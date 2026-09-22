package com.jax.funaethermod.client.model;

import com.jax.funaethermod.FunAetherMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class Anomaly221Model<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    new ResourceLocation(
                            FunAetherMod.MODID,
                            "anomaly_221"
                    ),
                    "main"
            );

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_leg;
    private final ModelPart right_leg;

    public Anomaly221Model(ModelPart root) {

        this.head = root.getChild("head");
        this.body = root.getChild("body");

        this.left_arm = root.getChild("left_arm");
        this.right_arm = root.getChild("right_arm");

        this.left_leg = root.getChild("left_leg");
        this.right_leg = root.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer() {

        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        /*
         * =========================================================
         * HEAD
         * =========================================================
         *
         * Blockbench pivot:
         * 0, 0, 0
         *
         * Keeping this as PartPose.ZERO means the head rotates
         * around the same pivot point used by the Blockbench model.
         */

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -4.0F,
                                -8.0F,
                                -3.0F,
                                8.0F,
                                8.0F,
                                8.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.ZERO
        );

        /*
         * =========================================================
         * BODY
         * =========================================================
         */

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(
                                -4.0F,
                                0.0F,
                                -1.0F,
                                8.0F,
                                12.0F,
                                4.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.ZERO
        );

        /*
         * =========================================================
         * LEFT ARM
         * =========================================================
         *
         * This uses the Blockbench child pivot and rotation.
         */

        PartDefinition leftArm =
                root.addOrReplaceChild(
                        "left_arm",
                        CubeListBuilder.create(),
                        PartPose.offset(
                                -5.0F,
                                2.0F,
                                0.0F
                        )
                );

        leftArm.addOrReplaceChild(
                "left_arm_r1",
                CubeListBuilder.create()
                        .texOffs(24, 16)
                        .addBox(
                                -11.0F,
                                -24.0F,
                                -1.0F,
                                4.0F,
                                12.0F,
                                4.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(
                        8.0F,
                        22.0F,
                        -1.0F,
                        -0.0436F,
                        0.0F,
                        0.0F
                )
        );

        /*
         * =========================================================
         * RIGHT ARM
         * =========================================================
         */

        root.addOrReplaceChild(
                "right_arm",
                CubeListBuilder.create()
                        .texOffs(0, 32)
                        .addBox(
                                -1.0F,
                                -2.0F,
                                -1.0F,
                                4.0F,
                                12.0F,
                                4.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        5.0F,
                        2.0F,
                        0.0F
                )
        );

        /*
         * =========================================================
         * LEFT LEG
         * =========================================================
         */

        root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(
                                -2.0F,
                                0.0F,
                                -1.0F,
                                4.0F,
                                12.0F,
                                4.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        -2.0F,
                        12.0F,
                        0.0F
                )
        );

        /*
         * =========================================================
         * RIGHT LEG
         * =========================================================
         */

        root.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create()
                        .texOffs(16, 32)
                        .addBox(
                                -2.0F,
                                0.0F,
                                -1.0F,
                                4.0F,
                                12.0F,
                                4.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        2.0F,
                        12.0F,
                        0.0F
                )
        );

        return LayerDefinition.create(
                meshDefinition,
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
         * Anomaly 221's head follows where it is looking.
         *
         * Because the head pivot is preserved from Blockbench,
         * the head rotates around the intended point instead of
         * appearing to slide away from the body.
         */

        this.head.yRot =
                netHeadYaw * ((float) Math.PI / 180F);

        this.head.xRot =
                headPitch * ((float) Math.PI / 180F);

        /*
         * =========================================================
         * WALKING ANIMATION
         * =========================================================
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
                Mth.cos(limbSwing * 0.6662F)
                        * 1.4F
                        * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer vertexConsumer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {

        head.render(
                poseStack,
                vertexConsumer,
                packedLight,
                packedOverlay,
                red,
                green,
                blue,
                alpha
        );

        body.render(
                poseStack,
                vertexConsumer,
                packedLight,
                packedOverlay,
                red,
                green,
                blue,
                alpha
        );

        left_arm.render(
                poseStack,
                vertexConsumer,
                packedLight,
                packedOverlay,
                red,
                green,
                blue,
                alpha
        );

        right_arm.render(
                poseStack,
                vertexConsumer,
                packedLight,
                packedOverlay,
                red,
                green,
                blue,
                alpha
        );

        left_leg.render(
                poseStack,
                vertexConsumer,
                packedLight,
                packedOverlay,
                red,
                green,
                blue,
                alpha
        );

        right_leg.render(
                poseStack,
                vertexConsumer,
                packedLight,
                packedOverlay,
                red,
                green,
                blue,
                alpha
        );
    }
}