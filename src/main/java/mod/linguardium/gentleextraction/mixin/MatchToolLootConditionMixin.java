package mod.linguardium.gentleextraction.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.item.ItemPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MatchToolLootCondition.class)
public class MatchToolLootConditionMixin {
    @WrapOperation(method="test(Lnet/minecraft/loot/context/LootContext;)Z",at=@At(value="INVOKE",target="Lnet/minecraft/predicate/item/ItemPredicate;test(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean ForceSilkTouchToolWhenCrouching(ItemPredicate instance, ItemStack stack, Operation<Boolean> original, LootContext context) {
        if (context.hasParameter(LootContextParameters.THIS_ENTITY) && context.get(LootContextParameters.THIS_ENTITY) instanceof PlayerEntity player) {
            if (player.isSneaking()) {
                if (stack == null || stack.isEmpty()) {
                    stack = new ItemStack(Items.AIR);
                }
                stack.addEnchantment(Enchantments.SILK_TOUCH,1);
            }
        }
        return original.call(instance,stack);
    }
}
