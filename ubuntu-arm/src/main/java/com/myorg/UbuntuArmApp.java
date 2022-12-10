package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class UbuntuArmApp {
    public static void main(final String[] args) {
        App app = new App();

        new UbuntuArmStack(app, "UbuntuArmStack", StackProps.builder()
                .env(Environment.builder()
                        .account("948431174004")
                        .region("eu-west-1")
                        .build())
                .build());

        app.synth();
    }
}

