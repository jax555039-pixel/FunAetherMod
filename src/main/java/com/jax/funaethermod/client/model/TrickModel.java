package com.jax.funaethermod.client.model;

import com.jax.funaethermod.FunAetherMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class TrickModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    new ResourceLocation(
                            FunAetherMod.MODID,
                            "trick"
                    ),
                    "main"
            );

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_leg;
    private final ModelPart right_leg;

    private final ModelPart wire_1;
    private final ModelPart wire_2;
    private final ModelPart wire_3;

    public TrickModel(ModelPart root) {

        this.head = root.getChild("head");
        this.body = root.getChild("body");

        this.left_arm = root.getChild("left_arm");
        this.right_arm = root.getChild("right_arm");

        this.left_leg = root.getChild("left_leg");
        this.right_leg = root.getChild("right_leg");

        this.wire_1 = root.getChild("wire_1");
        this.wire_2 = root.getChild("wire_2");
        this.wire_3 = root.getChild("wire_3");
    }

    public static LayerDefinition createBodyLayer() {

        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        /*
         * =========================================================
         * HEAD
         * =========================================================
         */

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -7.0F,
                                -35.0F,
                                -1.0F,
                                8,
                                8,
                                8,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        0.0F,
                        24.0F,
                        -4.0F
                )
        );

        /*
         * =========================================================
         * BODY
         * =========================================================
         */

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(16, 16)
                        .addBox(
                                -7.0F,
                                -27.0F,
                                1.0F,
                                8,
                                12,
                                4,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        0.0F,
                        24.0F,
                        -4.0F
                )
        );

        /*
         * =========================================================
         * LEFT ARM
         *
         * 4 x 20 x 4
         * =========================================================
         */

        root.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .addBox(
                                1.0F,
                                -27.0F,
                                1.0F,
                                4,
                                20,
                                4,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        0.0F,
                        24.0F,
                        -4.0F
                )
        );

        /*
         * =========================================================
         * RIGHT ARM
         *
         * 4 x 20 x 4
         * =========================================================
         */

        root.addOrReplaceChild(
                "right_arm",
                CubeListBuilder.create()
                        .texOffs(32, 40)
                        .addBox(
                                -11.0F,
                                -27.0F,
                                1.0F,
                                4,
                                20,
                                4,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        0.0F,
                        24.0F,
                        -4.0F
                )
        );

        /*
         * =========================================================
         * LEFT LEG
         *
         * 4 x 15 x 4
         * =========================================================
         */

        root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(
                                -7.0F,
                                -15.0F,
                                1.0F,
                                4,
                                15,
                                4,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        0.0F,
                        24.0F,
                        -4.0F
                )
        );

        /*
         * =========================================================
         * RIGHT LEG
         *
         * 4 x 15 x 4
         * =========================================================
         */

        root.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create()
                        .texOffs(16, 45)
                        .addBox(
                                -3.0F,
                                -15.0F,
                                1.0F,
                                4,
                                15,
                                4,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        0.0F,
                        24.0F,
                        -4.0F
                )
        );

        /*
         * =========================================================
         * WIRE 1
         * =========================================================
         */

        PartDefinition wire1 =
                root.addOrReplaceChild(
                        "wire_1",
                        CubeListBuilder.create(),
                        PartPose.offset(
                                0.0F,
                                24.0F,
                                0.0F
                        )
                );

        wire1.addOrReplaceChild(
                "wire_2_r1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -1.0F,
                                -2.0F,
                                -1.0F,
                                2,
                                5,
                                2,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(
                        8.0F,
                        -34.0F,
                        0.0F,
                        0.0F,
                        0.0F,
                        0.0873F
                )
        );

        wire1.addOrReplaceChild(
                "wire_1_r1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -1.0F,
                                3.0F,
                                -1.0F,
                                2,
                                9,
                                2,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(
                        8.0F,
                        -34.0F,
                        0.0F,
                        0.0F,
                        0.0F,
                        0.3927F
                )
        );

        /*
         * =========================================================
         * WIRE 2
         * =========================================================
         */

        PartDefinition wire2 =
                root.addOrReplaceChild(
                        "wire_2",
                        CubeListBuilder.create(),
                        PartPose.offset(
                                0.0F,
                                24.0F,
                                0.0F
                        )
                );

        wire2.addOrReplaceChild(
                "wire_4_r1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -1.0F,
                                -9.0F,
                                -1.0F,
                                2,
                                8,
                                2,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(
                        -16.0F,
                        -22.0F,
                        0.0F,
                        0.0F,
                        0.0F,
                        2.3126F
                )
        );

        wire2.addOrReplaceChild(
                "wire_3_r1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -1.0F,
                                -1.0F,
                                -1.0F,
                                2,
                                5,
                                2,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(
                        -16.0F,
                        -22.0F,
                        0.0F,
                        2.4657F,
                        -1.3875F,
                        -1.6186F
                )
        );

        /*
         * =========================================================
         * WIRE 3
         * =========================================================
         */

        PartDefinition wire3 =
                root.addOrReplaceChild(
                        "wire_3",
                        CubeListBuilder.create(),
                        PartPose.offset(
                                11.0F,
                                24.0F,
                                0.0F
                        )
                );

        wire3.addOrReplaceChild(
                "wire_6_r1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -1.0F,
                                -12.0F,
                                -1.0F,
                                1,
                                12,
                                2,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(
                        0.0F,
                        -5.0F,
                        0.0F,
                        0.0163F,
                        -0.0186F,
                        -0.7408F
                )
        );

        wire3.addOrReplaceChild(
                "wire_5_r1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                0.0F,
                                -12.0F,
                                -1.0F,
                                1,
                                12,
                                2,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(
                        0.0F,
                        -5.0F,
                        0.0F,
                        0.0F,
                        0.0F,
                        -0.48F
                )
        );

        return LayerDefinition.create(
                mesh,
                64,
                64
        );
    }

    @Override
    public void setupAnim(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {

        /*
         * =========================================================
         * HEAD
         * =========================================================
         *
         * Trick's head follows where he is looking.
         */

        this.head.yRot =
                netHeadYaw * ((float) Math.PI / 180F);

        this.head.xRot =
                headPitch * ((float) Math.PI / 180F);


        /*
         * =========================================================
         * WALKING ANIMATION
         * =========================================================
         *
         * Same basic animation as PoorBoy, but the longer
         * arms and legs retain their Trick proportions.
         */

        this.right_arm.xRot =
                Mth.cos(limbSwing * 0.6662F)
                        * 1.4F
                        * limbSwingAmount;

        this.left_arm.xRot =
                Mth.cos(
                        limbSwing * 0.6662F
                                + (float) Math.PI
                )
                        * 1.4F
                        * limbSwingAmount;

        this.right_leg.xRot =
                Mth.cos(
                        limbSwing * 0.6662F
                                + (float) Math.PI
                )
                        * 1.4F
                        * limbSwingAmount;

        this.left_leg.xRot =
                Mth.cos(
                        limbSwing * 0.6662F
                )
                        * 1.4F
                        * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {

        head.render(
                poseStack,
                buffer,
                packedLight,
                packedOverlay
        );

        body.render(
                poseStack,
                buffer,
                packedLight,
                packedOverlay
        );

        left_arm.render(
                poseStack,
                buffer,
                packedLight,
                packedOverlay
        );

        right_arm.render(
                poseStack,
                buffer,
                packedLight,
                packedOverlay
        );

        left_leg.render(
                poseStack,
                buffer,
                packedLight,
                packedOverlay
        );

        right_leg.render(
                poseStack,
                buffer,
                packedLight,
                packedOverlay
        );

        wire_1.render(
                poseStack,
                buffer,
                packedLight,
                packedOverlay
        );

        wire_2.render(
                poseStack,
                buffer,
                packedLight,
                packedOverlay
        );

        wire_3.render(
                poseStack,
                buffer,
                packedLight,
                packedOverlay
        );
    }
}