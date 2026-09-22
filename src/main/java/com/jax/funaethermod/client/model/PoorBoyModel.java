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

public class PoorBoyModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    new ResourceLocation(
                            FunAetherMod.MODID,
                            "poorboymodel"
                    ),
                    "main"
            );

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_leg;
    private final ModelPart right_leg;

    public PoorBoyModel(ModelPart root) {

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
                        .texOffs(16, 16)
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
         * LEFT ARM
         * =========================================================
         */

        root.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .addBox(
                                -3.0F,
                                -2.0F,
                                -1.0F,
                                4.0F,
                                12.0F,
                                4.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(
                        -5.0F,
                        2.0F,
                        -1.0F
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
                        .texOffs(32, 48)
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
         * LEFT LEG
         * =========================================================
         */

        root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create()
                        .texOffs(16, 48)
                        .addBox(
                                -2.0F,
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
         * RIGHT LEG
         * =========================================================
         *
         * Blockbench gave this leg a slight forward rotation.
         * Preserve it exactly.
         */

        root.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(
                                -2.0F,
                                0.0F,
                                -2.0F,
                                4.0F,
                                12.0F,
                                4.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(
                        2.0F,
                        12.0F,
                        0.0F,
                        0.0436F,
                        0.0F,
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
         * No additional translation is applied.
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

/*
=========================================================
LEARNING CORNER
=========================================================

This model is directly based on the Blockbench export.

The important part for the head is:

    PartPose.offset(0, 0, 0)

This gives the head a pivot at the model origin, just like
the Blockbench model.

The Java animation then only rotates the head:

    head.yRot
    head.xRot

It does NOT move the head backward or forward.

The slight right-leg rotation from Blockbench is also preserved:

    0.0436F

The other body-part pivots are kept exactly as exported.

If the head now behaves correctly like the Real model, the
Blockbench pivot setup was indeed the important difference.
*/