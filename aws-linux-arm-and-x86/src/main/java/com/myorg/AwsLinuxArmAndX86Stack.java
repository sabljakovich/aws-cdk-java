package com.myorg;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.*;
import software.constructs.Construct;

public class AwsLinuxArmAndX86Stack extends Stack {
    public AwsLinuxArmAndX86Stack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AwsLinuxArmAndX86Stack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);


        Vpc vpc = Vpc.Builder.create(this, id + "-vpc")
                .vpcName(id + "-vpc")
                .natGateways(0)
                .build();


        final ISecurityGroup securityGroup = SecurityGroup.Builder.create(this, id + "-sg")
                .securityGroupName(id)
                .vpc(vpc)
                .build();

        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(22));


        final Instance armEC2Instance = Instance.Builder.create(this, id + "-arm-ec2")
                .instanceName(id + "-arm-ec2")
                .machineImage(MachineImage.latestAmazonLinux(
                        AmazonLinuxImageProps.builder()
                                .cpuType(AmazonLinuxCpuType.ARM_64)
                                .generation(AmazonLinuxGeneration.AMAZON_LINUX_2)
                                .build()
                ))
                .securityGroup(securityGroup)
                .instanceType(InstanceType.of(
                        InstanceClass.BURSTABLE4_GRAVITON,
                        InstanceSize.SMALL
                ))
                .vpcSubnets(
                        SubnetSelection.builder()
                                .subnetType(SubnetType.PUBLIC)
                                .build()
                )
                .vpc(vpc)
                .build();


        final Instance x86EC2Instance = Instance.Builder.create(this, id + "-x86-ec2")
                .instanceName(id + "-x86-ec2")
                .machineImage(MachineImage.latestAmazonLinux(
                        AmazonLinuxImageProps.builder()
                                .cpuType(AmazonLinuxCpuType.X86_64)
                                .generation(AmazonLinuxGeneration.AMAZON_LINUX_2)
                                .build()
                ))
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


        final Instance ec2InstanceWithDefaultSettings = Instance.Builder.create(this, id + "-default-settings")
                .instanceName(id + "-default-settings")
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

    }
}
