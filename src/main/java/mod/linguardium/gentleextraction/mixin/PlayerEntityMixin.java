package mod.linguardium.gentleextraction.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import mod.linguardium.gentleextraction.Config;
import mod.linguardium.gentleextraction.GentleExtraction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method="getBlockBreakingSpeed",at=@At("RETURN"))
    private float modifyBreakSpeed(float original, BlockState state) {
        if (!isSneaking()) return original;
        if (!GentleExtraction.isWhiteListed(state)) return original;
        if (GentleExtraction.isBlackListed(state)) return original;
        return original / Config.INSTANCE.breakDivisor;
    }

}
