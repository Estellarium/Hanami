package com.teamaurora.hanami.common.entity;

import com.teamaurora.hanami.core.registry.HanamiEntities;
import com.teamaurora.hanami.core.registry.HanamiItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class SakuraBlossomEntity extends LivingEntity {
    private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(SakuraBlossomEntity.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Boolean> WILD = EntityDataManager.createKey(SakuraBlossomEntity.class, DataSerializers.BOOLEAN);
    private boolean playedSound;

    public SakuraBlossomEntity(EntityType<? extends SakuraBlossomEntity> blossom, World worldIn) {
        super(HanamiEntities.SAKURA_BLOSSOM.get(), worldIn);
    }

    public SakuraBlossomEntity(World worldIn, BlockPos origin, double x, double y, double z, boolean wild) {
        this(HanamiEntities.SAKURA_BLOSSOM.get(), worldIn);
        this.setHealth(1);
        this.setOrigin(new BlockPos(origin));
        this.setWild(wild);
        this.setPosition(x + 0.5D, y, z + 0.5D);
        this.setNoGravity(true);
        this.prevRenderYawOffset = 180.0F;
        this.renderYawOffset = 180.0F;
        this.rotationYaw = 180.0F;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ORIGIN, BlockPos.ZERO);
        this.dataManager.register(WILD, false);
    }

    @Override
    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putLong("OriginPos", this.getOrigin().toLong());
        nbt.putBoolean("Wild", this.getWild());
    }

    @Override
    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        this.setOrigin(BlockPos.fromLong(nbt.getLong("OriginPos")));
        this.setWild(nbt.getBoolean("Wild"));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWild()) {
            this.renderYawOffset = this.prevRenderYawOffset = this.prevRenderYawOffset + 3.0F;
        } else {
            this.renderYawOffset = this.prevRenderYawOffset = 180.0F;
        }
        this.rotationYaw = this.prevRotationYaw = 180.0F;

        if (this.getWild()) {
            this.setMotion(0, this.getBreeze(this.getPosX(), this.getPosZ()), -0.125F);
        } else {
            this.setMotion(0, -0.075F, 0);
        }

        if(this.isBlockBlockingPath()) {
            this.remove();
        }

        this.clearActivePotions();
        this.extinguish();

        if(!this.world.isRemote) {
            if (!this.playedSound) {
                if (!this.getWild()) this.world.setEntityState(this, (byte)1);
                this.playedSound = true;
            }
        }
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        if(!this.world.isRemote) {
            Block.spawnAsEntity(this.world, this.func_233580_cy_(), new ItemStack(HanamiItems.SAKURA_BLOSSOM.get()));
        }
        this.remove();
        return true;
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (damageSrc.isProjectile()) {
            if(!this.getEntityWorld().isRemote) {
                Block.spawnAsEntity(this.world, this.func_233580_cy_(), new ItemStack(HanamiItems.SAKURA_BLOSSOM.get()));
            }
            this.remove();
        } else if (damageSrc == DamageSource.IN_WALL) {
            this.remove();
        }
        super.damageEntity(damageSrc, damageAmount);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 1) {
            this.playSound(SoundEvents.BLOCK_WOOL_BREAK, 1.0F, 1.0F);
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.BLOCK_WOOL_BREAK;
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize size) {
        return size.height;
    }

    private double getBreeze(double x, double z) {
        return 0.0625F * Math.cos((x + z) * 0.05) - 0.0125F;
    }

    private boolean isBlockBlockingPath() {
        Vector3d eyePos = this.getPositionVec();
        return this.world.rayTraceBlocks(new RayTraceContext(
                eyePos,
                eyePos.add(this.getMotion()),
                RayTraceContext.BlockMode.OUTLINE,
                RayTraceContext.FluidMode.ANY,
                this
        )).getType() != RayTraceResult.Type.MISS;
    }

    public void setOrigin(BlockPos pos) {
        this.dataManager.set(ORIGIN, pos);
    }

    public BlockPos getOrigin() {
        return this.dataManager.get(ORIGIN);
    }

    public void setWild(boolean wild) {
        this.dataManager.set(WILD, wild);
    }

    public boolean getWild() {
        return this.dataManager.get(WILD);
    }

    @Override
    public void applyKnockback(float strengthIn, double ratioXIn, double ratioZIn) {}

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ILLAGER;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return entityIn.canBePushed() ? entityIn.getBoundingBox() : null;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getBoundingBox();
    }

    @Override
    protected float getWaterSlowDown() {
        return 0;
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean startRiding(Entity entityIn, boolean force) {
        return false;
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {}

    @Override
    public HandSide getPrimaryHand() {
        return HandSide.RIGHT;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_().func_233815_a_(Attributes.MAX_HEALTH, 1.0D);
    }
}
