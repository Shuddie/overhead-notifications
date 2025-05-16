package com.example;

import net.runelite.client.config.*;
import java.awt.*;

@ConfigGroup("overheadnotifications")
public interface OverheadNotificationsConfig extends Config
{
	@ConfigItem(keyName = "overheadDisplayTime", name = "Overhead Display Time", description = "Time to display overhead text (in seconds)")
	default int overheadDisplayTime() { return 3; }
	// ====== SECTION: Boosts ======
	@ConfigSection(name = "Boosts", description = "Settings for boosts-related potions", position = 0)
	String boostsSection = "boostsSection";

	@ConfigItem(keyName = "enableOverload", name = "Enable Overload", description = "Enable overhead for Overload", section = boostsSection, position = 0)
	default boolean enableOverload() { return false; }
	@ConfigItem(keyName = "overloadColor", name = "Overload Color", description = "Text color for Overload", section = boostsSection, position = 1)
	default Color overloadColor() { return Color.RED; }
	@ConfigItem(keyName = "overloadMessage", name = "Overload Message", description = "Message for Overload", section = boostsSection, position = 2)
	default String overloadMessage() { return "Overload Expired!"; }

	@ConfigItem(keyName = "enableSmellingSalt", name = "Enable Smelling Salt", description = "Enable overhead for Smelling Salt", section = boostsSection, position = 3)
	default boolean enableSmellingSalt() { return false; }
	@ConfigItem(keyName = "smellingSaltColor", name = "Smelling Salt Color", description = "Text color for Smelling Salt", section = boostsSection, position = 4)
	default Color smellingSaltColor() { return Color.ORANGE; }
	@ConfigItem(keyName = "smellingSaltMessage", name = "Smelling Salt Message", description = "Message for Smelling Salt", section = boostsSection, position = 5)
	default String smellingSaltMessage() { return "Smelling Salt Expired!"; }

	@ConfigItem(keyName = "enableDivinePot", name = "Enable Divine Pot", description = "Enable overhead for any divine potion", section = boostsSection, position = 6)
	default boolean enableDivinePot() { return false; }
	@ConfigItem(keyName = "divinePotColor", name = "Divine Pot Color", description = "Text color for divine potion overhead text", section = boostsSection, position = 7)
	default Color divinePotColor() { return Color.GREEN; }
	@ConfigItem(keyName = "divinePotMessage", name = "Divine Pot Message", description = "Message to display for any divine potion", section = boostsSection, position = 8)
	default String divinePotMessage() { return "Divine Expired!"; }

	// ====== SECTION: Prayer ======
	@ConfigSection(name = "Prayer", description = "Settings for prayer-related potions", position = 2)
	String prayerSection = "prayerSection";

	@ConfigItem(keyName = "enablePrayerRegen", name = "Enable Prayer Regen", description = "Enable overhead for Prayer Regeneration", section = prayerSection, position = 0)
	default boolean enablePrayerRegen() { return false; }
	@ConfigItem(keyName = "prayerRegenColor", name = "Prayer Regen Color", description = "Text color for Prayer Regen", section = prayerSection, position = 1)
	default Color prayerRegenColor() { return Color.PINK; }
	@ConfigItem(keyName = "prayerRegenMessage", name = "Prayer Regen Message", description = "Message for Prayer Regen", section = prayerSection, position = 2)
	default String prayerRegenMessage() { return "Prayer regeneration potion Expired!"; }

	@ConfigItem(keyName = "enablePrayerEnhance", name = "Enable Prayer Enhance", description = "Enable overhead for Prayer Enhance", section = prayerSection, position = 3)
	default boolean enablePrayerEnhance() { return false; }
	@ConfigItem(keyName = "prayerEnhanceColor", name = "Prayer Enhance Color", description = "Text color for Prayer Enhance", section = prayerSection, position = 4)
	default Color prayerEnhanceColor() { return Color.PINK; }
	@ConfigItem(keyName = "prayerEnhanceMessage", name = "Prayer Enhance Message", description = "Message for Prayer Enhance", section = prayerSection, position = 5)
	default String prayerEnhanceMessage() { return "Prayer enhance potion Expired!"; }

	// ====== SECTION: Other ======
	@ConfigSection(name = "Other", description = "Other special cases", position = 3)
	String otherSection = "otherSection";

	@ConfigItem(keyName = "enableThrall", name = "Enable Thrall", description = "Enable overhead for Thralls", section = otherSection, position = 0)
	default boolean enableThrall() { return false; }
	@ConfigItem(keyName = "thrallColor", name = "Thrall Color", description = "Text color for Thralls", section = otherSection, position = 1)
	default Color thrallColor() { return Color.CYAN; }
	@ConfigItem(keyName = "thrallMessage", name = "Thrall Message", description = "Message for Thralls", section = otherSection, position = 2)
	default String thrallMessage() { return "Thrall Expired!"; }

	@ConfigItem(keyName = "enableDeathCharge", name = "Enable DeathCharge", description = "Enable overhead for DeathCharge", section = otherSection, position = 3)
	default boolean enableDeathCharge() { return false; }
	@ConfigItem(keyName = "DeathChargeColor", name = "DeathCharge Color", description = "Text color for DeathCharge", section = otherSection, position = 4)
	default Color deathchargeColor() { return Color.RED; }
	@ConfigItem(keyName = "DeathChargeMessage", name = "DeathCharge Message", description = "Message for DeathCharge", section = otherSection, position = 5)
	default String deathchargeMessage() { return "Death Charge Ready!"; }

	@ConfigItem(keyName = "enableMarkOfDarkness", name = "Enable Mark of Darkness", description = "Enable overhead for Mark of Darkness", section = otherSection, position = 6)
	default boolean enableMarkOfDarkness() { return false; }

	@ConfigItem(keyName = "markOfDarknessColor", name = "Mark of Darkness Color", description = "Text color for Mark of Darkness", section = otherSection, position = 7)
	default Color markOfDarknessColor() { return Color.BLUE; }

	@ConfigItem(keyName = "markOfDarknessMessage", name = "Mark of Darkness Message", description = "Message for Mark of Darkness", section = otherSection, position = 8)
	default String markOfDarknessMessage() { return "Mark of Darkness expired!"; }


	@ConfigItem(keyName = "enableSaturatedHeart", name = "Enable Saturated Heart", description = "Enable overhead for Saturated Heart", section = otherSection, position = 9)
	default boolean enableSaturatedHeart() { return false; }
	@ConfigItem(keyName = "saturatedHeartColor", name = "Saturated Heart Color", description = "Text color for Saturated Heart", section = otherSection, position = 10)
	default Color saturatedHeartColor() { return Color.MAGENTA; }
	@ConfigItem(keyName = "saturatedHeartMessage", name = "Saturated Heart Message", description = "Message for Saturated Heart", section = otherSection, position = 11)
	default String saturatedHeartMessage() { return "Saturated Heart Expired!"; }

	@ConfigItem(keyName = "enableStamina", name = "Enable Stamina", description = "Enable overhead for Stamina", section = otherSection, position = 12)
	default boolean enableStamina() { return false; }
	@ConfigItem(keyName = "staminaColor", name = "Stamina Color", description = "Text color for Stamina", section = otherSection, position = 13)
	default Color staminaColor() { return Color.YELLOW; }
	@ConfigItem(keyName = "staminaMessage", name = "Stamina Message", description = "Message for Stamina", section = otherSection, position = 14)
	default String staminaMessage() { return "Stamina potion Expired!"; }
}