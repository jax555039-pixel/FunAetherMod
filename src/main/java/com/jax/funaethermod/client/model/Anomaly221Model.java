package com.jax.funaethermod.client.model;

import com.jax.funaethermod.FunAetherMod;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class Anomaly221Model<T extends Entity> extends EntityModel<T> {

    /*
     * =========================================================
     * MODEL LAYER
     * =========================================================
     *
     * This must match the layer location used by
     * Anomaly221Renderer.
     */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    new ResourceLocation(
                            FunAetherMod.MODID,
                            "anomaly221model"
                    ),
                    "main"
            );


    /*
     * =========================================================
     * MODEL PARTS
     * =========================================================
     */

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_leg;
    private final ModelPart right_leg;


    /*
     * =========================================================
     * CONSTRUCTOR
     * =========================================================
     */

    public Anomaly221Model(ModelPart root) {

        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.left_arm = root.getChild("left_arm");
        this.right_arm = root.getChild("right_arm");
        this.left_leg = root.getChild("left_leg");
        this.right_leg = root.getChild("right_leg");
    }


    /*
     * =========================================================
     * MODEL GEOMETRY
     * =========================================================
     *
     * The pivot positions exported by Blockbench are preserved.
     */

    public static LayerDefinition createBodyLayer() {

        MeshDefinition meshdefinition =
                new MeshDefinition();

        PartDefinition partdefinition =
                meshdefinition.getRoot();


        /*
         * HEAD
         *
         * Pivot:
         * 0, 24, 0
         */
        PartDefinition head =
                partdefinition.addOrReplaceChild(
                        "head",

                        CubeListBuilder.create()
                                .texOffs(0, 0)
                                .addBox(
                                        -7.0F,
                                        -32.0F,
                                        -3.0F,
                                        8.0F,
                                        8.0F,
                                        8.0F,
                                        new CubeDeformation(0.0F)
                                ),

                        PartPose.offset(
                                0.0F,
                                24.0F,
                                0.0F
                        )
                );


        /*
         * BODY
         *
         * Pivot:
         * 0, 24, 0
         */
        PartDefinition body =
                partdefinition.addOrReplaceChild(
                        "body",

                        CubeListBuilder.create()
                                .texOffs(0, 16)
                                .addBox(
                                        -7.0F,
                                        -24.0F,
                                        -1.0F,
                                        8.0F,
                                        12.0F,
                                        4.0F,
                                        new CubeDeformation(0.0F)
                                ),

                        PartPose.offset(
                                0.0F,
                                24.0F,
                                0.0F
                        )
                );


        /*
         * LEFT ARM
         *
         * Blockbench added a pivot/rotation here.
         *
         * Rotation:
         * X = 0.0436 radians
         */
        PartDefinition left_arm =
                partdefinition.addOrReplaceChild(
                        "left_arm",

                        CubeListBuilder.create()
                                .texOffs(24, 16)
                                .addBox(
                                        -6.0F,
                                        -2.0F,
                                        -1.0F,
                                        4.0F,
                                        12.0F,
                                        4.0F,
                                        new CubeDeformation(0.0F)
                                ),

                        PartPose.offsetAndRotation(
                                -5.0F,
                                2.0F,
                                0.0F,
                                0.0436F,
                                0.0F,
                                0.0F
                        )
                );


        /*
         * RIGHT ARM
         *
         * Pivot:
         * 4, 2, 0
         */
        PartDefinition right_arm =
                partdefinition.addOrReplaceChild(
                        "right_arm",

                        CubeListBuilder.create()
                                .texOffs(0, 32)
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
                                4.0F,
                                2.0F,
                                0.0F
                        )
                );


        /*
         * LEFT LEG
         *
         * Pivot:
         * -2, 12, 0
         */
        PartDefinition left_leg =
                partdefinition.addOrReplaceChild(
                        "left_leg",

                        CubeListBuilder.create()
                                .texOffs(32, 0)
                                .addBox(
                                        -5.0F,
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
         * RIGHT LEG
         *
         * Pivot:
         * 2, 12, 0
         */
        PartDefinition right_leg =
                partdefinition.addOrReplaceChild(
                        "right_leg",

                        CubeListBuilder.create()
                                .texOffs(16, 32)
                                .addBox(
                                        -5.0F,
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


        /*
         * =========================================================
         * TEXTURE SIZE
         * =========================================================
         *
         * Blockbench exported this model using a 64x64 texture.
         */
        return LayerDefinition.create(
                meshdefinition,
                64,
                64
        );
    }


    /*
     * =========================================================
     * ANIMATION
     * =========================================================
     */

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
         * Reset rotations every frame.
         *
         * This is important once we start adding walking
         * animations later.
         */
        this.head.xRot = 0.0F;
        this.head.yRot = 0.0F;
        this.head.zRot = 0.0F;

        this.body.xRot = 0.0F;
        this.body.yRot = 0.0F;
        this.body.zRot = 0.0F;

        /*
         * Keep the Blockbench pivot rotation on the left arm.
         */
        this.left_arm.xRot = 0.0436F;
        this.left_arm.yRot = 0.0F;
        this.left_arm.zRot = 0.0F;

        this.right_arm.xRot = 0.0F;
        this.right_arm.yRot = 0.0F;
        this.right_arm.zRot = 0.0F;

        this.left_leg.xRot = 0.0F;
        this.left_leg.yRot = 0.0F;
        this.left_leg.zRot = 0.0F;

        this.right_leg.xRot = 0.0F;
        this.right_leg.yRot = 0.0F;
        this.right_leg.zRot = 0.0F;


        /*
         * =====================================================
         * HEAD TRACKING
         * =====================================================
         *
         * Makes the model's head follow the direction that
         * the entity is looking.
         */
        this.head.yRot =
                netHeadYaw * ((float) Math.PI / 180F);

        this.head.xRot =
                headPitch * ((float) Math.PI / 180F);
    }


    /*
     * =========================================================
     * RENDER
     * =========================================================
     */

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