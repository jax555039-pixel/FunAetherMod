package com.jax.funaethermod.client.model;

import com.jax.funaethermod.FunAetherMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
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
                    new ResourceLocation(FunAetherMod.MODID, "poorboy"),
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

        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -32.0F, -4.0F, 8, 8, 8),
                PartPose.offset(0, 24, 0)
        );

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(16, 16)
                        .addBox(-4.0F, -24.0F, -2.0F, 8, 12, 4),
                PartPose.offset(0, 24, 0)
        );

        root.addOrReplaceChild(
                "right_arm",
                CubeListBuilder.create()
                        .texOffs(32, 48)
                        .addBox(4.0F, -24.0F, -2.0F, 4, 12, 4),
                PartPose.offset(0, 24, 0)
        );

        root.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .addBox(-8.0F, -24.0F, -2.0F, 4, 12, 4),
                PartPose.offset(0, 24, 0)
        );

        root.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(0.0F, -12.0F, -2.0F, 4, 12, 4),
                PartPose.offset(0, 24, 0)
        );

        root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create()
                        .texOffs(16, 48)
                        .addBox(-4.0F, -12.0F, -2.0F, 4, 12, 4),
                PartPose.offset(0, 24, 0)
        );

        return LayerDefinition.create(mesh, 64, 64);
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

        // Head follows where the entity looks.
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);

        // Walking animation.
        this.right_arm.xRot =
                Mth.cos(limbSwing * 0.6662F)
                        * 1.4F
                        * limbSwingAmount;

        this.left_arm.xRot =
                Mth.cos(limbSwing * 0.6662F + (float)Math.PI)
                        * 1.4F
                        * limbSwingAmount;

        this.right_leg.xRot =
                Mth.cos(limbSwing * 0.6662F + (float)Math.PI)
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
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {

        head.render(poseStack, buffer, packedLight, packedOverlay);
        body.render(poseStack, buffer, packedLight, packedOverlay);
        left_arm.render(poseStack, buffer, packedLight, packedOverlay);
        right_arm.render(poseStack, buffer, packedLight, packedOverlay);
        left_leg.render(poseStack, buffer, packedLight, packedOverlay);
        right_leg.render(poseStack, buffer, packedLight, packedOverlay);
    }
}