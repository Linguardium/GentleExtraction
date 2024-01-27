package mod.linguardium.gentleextraction.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.linguardium.gentleextraction.GentleExtraction;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.item.ItemPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MatchToolLootCondition.class)
public class MatchToolLootConditionMixin {
    @WrapOperation(method="test(Lnet/minecraft/loot/context/LootContext;)Z",at=@At(value="INVOKE",target="Lnet/minecraft/predicate/item/ItemPredicate;test(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean ForceSilkTouchToolWhenCrouching(ItemPredicate instance, ItemStack stack, Operation<Boolean> original, LootContext context) {
        return original.call(instance,mutatedStack(context,stack));
    }
    @Unique
    private ItemStack mutatedStack(LootContext context, ItemStack original) {
        Entity e =  context.get(LootContextParameters.THIS_ENTITY);
        BlockState state = context.get(LootContextParameters.BLOCK_STATE);
        if (!(e instanceof PlayerEntity)) return original;
        if (state == null) return original;
        if (!e.isSneaking()) return original;
        if (GentleExtraction.isBlackListed(state)) return original;
        if (!GentleExtraction.isWhiteListed(state)) return original;
        ItemStack stack = original.copy();
        if (stack == null || stack.isEmpty()) {
            stack = new ItemStack(Items.AIR);
        }
        stack.addEnchantment(Enchantments.SILK_TOUCH,1);
        return stack;
    }
}
