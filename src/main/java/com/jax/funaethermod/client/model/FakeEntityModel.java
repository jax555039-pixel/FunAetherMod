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

public class FakeEntityModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    new ResourceLocation(
                            FunAetherMod.MODID,
                            "fake_entity"
                    ),
                    "main"
            );

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public FakeEntityModel(ModelPart root) {

        this.head = root.getChild("head");
        this.body = root.getChild("body");

        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");

        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
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
         * This matches the Real model's head pivot.
         */

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -4.0F,
                                -8.0F,
                                -4.0F,
                                8.0F,
                                8.0F,
                                8.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        0.0F,
                        0.0F,
                        0.0F
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
                        .texOffs(0, 16)
                        .addBox(
                                -4.0F,
                                0.0F,
                                -2.0F,
                                8.0F,
                                12.0F,
                                4.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        0.0F,
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
                        .texOffs(24, 16)
                        .addBox(
                                -1.0F,
                                -2.0F,
                                -2.0F,
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
         * LEFT ARM
         * =========================================================
         */

        root.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create()
                        .texOffs(0, 32)
                        .addBox(
                                -3.0F,
                                -2.0F,
                                -2.0F,
                                4.0F,
                                12.0F,
                                4.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        -5.0F,
                        2.0F,
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
                        .texOffs(32, 0)
                        .addBox(
                                2.0F,
                                0.0F,
                                -2.0F,
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
         * LEFT LEG
         * =========================================================
         */

        root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create()
                        .texOffs(16, 32)
                        .addBox(
                                -6.0F,
                                0.0F,
                                -2.0F,
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
         * The head rotates around its Blockbench pivot.
         * It does not move backward or forward.
         */

        this.head.yRot =
                netHeadYaw * ((float) Math.PI / 180F);

        this.head.xRot =
                headPitch * ((float) Math.PI / 180F);

        /*
         * =========================================================
         * WALKING
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
    }
}