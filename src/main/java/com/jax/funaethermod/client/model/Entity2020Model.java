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
import net.minecraft.world.entity.Entity;

public class Entity2020Model<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    new ResourceLocation(FunAetherMod.MODID, "entity2020"),
                    "main"
            );

    private final ModelPart head;
    private final ModelPart upper1;
    private final ModelPart upper2;
    private final ModelPart upper3;
    private final ModelPart upper4;
    private final ModelPart upper5;
    private final ModelPart middle1;
    private final ModelPart middle2;
    private final ModelPart lower1;
    private final ModelPart lower2;

    public Entity2020Model(ModelPart root) {

        this.head = root.getChild("head");
        this.upper1 = root.getChild("upper1");
        this.upper2 = root.getChild("upper2");
        this.upper3 = root.getChild("upper3");
        this.upper4 = root.getChild("upper4");
        this.upper5 = root.getChild("upper5");
        this.middle1 = root.getChild("middle1");
        this.middle2 = root.getChild("middle2");
        this.lower1 = root.getChild("lower1");
        this.lower2 = root.getChild("lower2");
    }

    public static LayerDefinition createBodyLayer() {

        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 53)
                        .addBox(-25.0F, -239.0F, -1.0F,
                                53.0F, 57.0F, 0.5F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "upper1",
                CubeListBuilder.create()
                        .texOffs(42, 111)
                        .addBox(-5.0F, -182.0F, -1.0F,
                                11.0F, 17.0F, 0.5F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "upper2",
                CubeListBuilder.create()
                        .texOffs(66, 111)
                        .addBox(8.0F, -166.0F, -1.0F,
                                11.0F, 17.0F, 0.5F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "upper3",
                CubeListBuilder.create()
                        .texOffs(90, 121)
                        .addBox(-13.0F, -166.0F, -2.0F,
                                11.0F, 17.0F, 0.5F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "upper4",
                CubeListBuilder.create()
                        .texOffs(114, 121)
                        .addBox(-3.0F, -166.0F, -1.0F,
                                11.0F, 17.0F, 0.5F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "upper5",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-44.0F, -149.0F, -1.0F,
                                99.0F, 43.0F, 0.5F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "middle1",
                CubeListBuilder.create()
                        .texOffs(108, 53)
                        .addBox(-26.0F, -106.0F, -1.0F,
                                57.0F, 33.0F, 0.5F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "middle2",
                CubeListBuilder.create()
                        .texOffs(108, 87)
                        .addBox(-17.0F, -73.0F, -1.0F,
                                40.0F, 33.0F, 0.5F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "lower1",
                CubeListBuilder.create()
                        .texOffs(0, 111)
                        .addBox(-5.0F, -41.0F, -1.0F,
                                20.0F, 33.0F, 0.5F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "lower2",
                CubeListBuilder.create()
                        .texOffs(0, 44)
                        .addBox(-43.0F, -8.0F, -1.0F,
                                98.0F, 8.0F, 0.5F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 256, 256);}
   @Override
public void setupAnim(
        T entity,
        float limbSwing,
        float limbSwingAmount,
        float ageInTicks,
        float netHeadYaw,
        float headPitch
) {
    // Entity 2020 has no animations
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

    head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    upper1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    upper2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    upper3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    upper4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    upper5.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    middle1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    middle2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    lower1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    lower2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
} }