package com.myorg;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.*;
import software.constructs.Construct;


public class AwsLinuxElasticIpStack extends Stack {
    public AwsLinuxElasticIpStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AwsLinuxElasticIpStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final Vpc vpc = Vpc.Builder.create(this, id + "-vpc")
                .vpcName(id + "-vpc")
                .natGateways(0) // Do not create any NATs
                .build();

        final ISecurityGroup securityGroup = SecurityGroup.Builder.create(this, id + "-sg")
                .securityGroupName(id)
                .vpc(vpc)
                .build();

        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(22));

        final Instance ec2Instance = Instance.Builder.create(this, id + "-ec2")
                .instanceName(id + "-ec2")
                .machineImage(MachineImage.latestAmazonLinux())
                .securityGroup(securityGroup)
                .instanceType(InstanceType.of(
                        InstanceClass.BURSTABLE3,
                        InstanceSize.MICRO
                ))
                .vpcSubnets(
                        SubnetSelection.builder()
                                .subnetType(SubnetType.PUBLIC)
                                .build()
                )
                .vpc(vpc)
                .build();

        final CfnEIP eip = new CfnEIP(this, id + "-eip");
        final CfnEIPAssociation eipAssociation = new CfnEIPAssociation(
                this,
                id + "-eip-association",
                CfnEIPAssociationProps.builder()
                        .eip(eip.getRef())
                        .instanceId(ec2Instance.getInstanceId())
                        .build()
        );
    }
}
