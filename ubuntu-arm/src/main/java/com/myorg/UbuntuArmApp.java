package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class UbuntuArmApp {
    public static void main(final String[] args) {
        App app = new App();

        new UbuntuArmStack(app, "UbuntuArmStack", StackProps.builder()
                .env(Environment.builder()
                        .account("000000000") // Replace with your account id
                        .region("eu-west-1") // Replace with your region
                        .build())
                .build());

        app.synth();
    }
}

