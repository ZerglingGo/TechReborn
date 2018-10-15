package techreborn.api.armor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ModularArmorUtils {

	public static boolean isUprgade(@Nonnull ItemStack stack) {
		return !stack.isEmpty() && stack.hasCapability(CapabilityArmorUpgrade.ARMOR_UPRGRADE_CAPABILITY, null);
	}

	public static IArmorUpgrade getArmorUprgade(@Nonnull ItemStack stack) {
		Validate.notNull(stack);
		return stack.getCapability(CapabilityArmorUpgrade.ARMOR_UPRGRADE_CAPABILITY, null);
	}

	public static UpgradeHolder getArmorUprgadeHolder(@Nonnull ItemStack stack, IModularArmorManager armorManager) {
		return new UpgradeHolder(stack, armorManager);
	}

	public static IModularArmorManager getManager(@Nonnull ItemStack stack) {
		Validate.isTrue(isModularArmor(stack));
		return stack.getCapability(CapabilityArmorUpgrade.ARMOR_MANAGER_CAPABILITY, null);
	}

	public static boolean isModularArmor(@Nonnull ItemStack stack){
		Validate.notNull(stack);
		if(stack.isEmpty()){
			return false;
		}
		return stack.hasCapability(CapabilityArmorUpgrade.ARMOR_MANAGER_CAPABILITY, null);
	}

	public static List<IModularArmorManager> getArmorOnPlayer(EntityPlayer player){
		return StreamSupport
			.stream(player.getArmorInventoryList().spliterator(), false)
			.filter(ModularArmorUtils::isModularArmor)
			.map(ModularArmorUtils::getManager)
			.collect(Collectors.toList());
	}

	public static List<IArmorUpgrade> getUpgradesOnPlayer(EntityPlayer player){
		return getArmorOnPlayer(player).stream()
			.flatMap((Function<IModularArmorManager, Stream<IArmorUpgrade>>) iModularArmorManager -> iModularArmorManager.getAllUprgades().stream())
			.collect(Collectors.toList());
	}

	//Similar to above, just returns the UpgradeHolder to allow for more things to be done
	public static List<UpgradeHolder> getHoldersOnPlayer(EntityPlayer player){
		return getArmorOnPlayer(player).stream()
			.flatMap((Function<IModularArmorManager, Stream<UpgradeHolder>>) iModularArmorManager -> iModularArmorManager.getAllHolders().stream())
			.collect(Collectors.toList());
	}

}