package com.shuddie.overhead;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.client.Notifier;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.awt.*;
import java.util.regex.Pattern;
import java.util.List;

@Slf4j
@PluginDescriptor(
        name = "Overhead Notification",
        description = "Shows overhead text when boosts expire",
        tags = {"overhead", "notification", "chat", "thrall", "overload", "boost"}
)
public class OverheadNotificationsPlugin extends Plugin
{
    private static final String MSG_OVERLOAD_WORN_OFF = "overload have worn off";
    private static final String MSG_SMELLING_SALT_WORN_OFF = "smelling salts has worn off";
    private static final String MSG_DIVINE_POTION_WORN_OFF = "divine potion have worn off";
    private static final String MSG_PRAYER_REGEN = "prayer regeneration";
    private static final String MSG_PRAYER_ENHANCE_WORN_OFF = "prayer enhance effect has worn off";
    private static final String MSG_THRALL_RETURNS = "thrall returns to the grave";
    private static final String MSG_MARK_OF_DARKNESS_FADED = "Mark of Darkness has faded away";
    private static final String MSG_SATURATED_HEART_WORN_OFF = "saturated heart have worn off";
    private static final String MSG_STAMINA_EXPIRED = "stamina enhancement has expired";
    private static final String MSG_SOULFLAMEHORN_1 = "encourages you with their Soulflame horn";
    private static final String MSG_SOULFLAMEHORN_2 = "You encourage nearby allies";
    private static final String MSG_SURGE_POTION_READY = "capable of drinking another dose of surge potion";
    public static final Pattern DEATH_CHARGE_ACTIVE =
            Pattern.compile("<col=[A-Fa-f\\d]+>Upon the death of your next foe, some of your special attack energy will be restored\\.</col>");

    public static final Pattern UPGRADED_DEATH_CHARGE_ACTIVE =
            Pattern.compile("<col=[A-Fa-f\\d]+>Upon the death of your next two foes, some of your special attack energy will be restored\\.</col>");

    @Inject
    private Client client;

    @Inject
    private Notifier notifier;

    @Inject
    private ChatMessageManager chatMessageManager;

    @Inject
    private OverheadNotificationsConfig config;

    @Inject
    private Overhead overhead;

    private String lastTrigger = null;
    private DeathChargeTimer timer = null;
    private List<TriggerRule> triggerRules;

    @Override
    protected void startUp()
    {
        log.info("Overhead Notification Plugin started!");
        timer = new DeathChargeTimer();

        triggerRules = List.of(
                new TriggerRule("Overload", MSG_OVERLOAD_WORN_OFF, config::enableOverload, config::overloadMessage, config::overloadColor, 0),
                new TriggerRule("Smelling Salt", MSG_SMELLING_SALT_WORN_OFF, config::enableSmellingSalt, config::smellingSaltMessage, config::smellingSaltColor, 0),
                new TriggerRule("Divine Pot", MSG_DIVINE_POTION_WORN_OFF, config::enableDivinePot, config::divinePotMessage, config::divinePotColor, 0),
                new TriggerRule("Prayer Regen", MSG_PRAYER_REGEN, config::enablePrayerRegen, config::prayerRegenMessage, config::prayerRegenColor, 0),
                new TriggerRule("Prayer Enhance", MSG_PRAYER_ENHANCE_WORN_OFF, config::enablePrayerEnhance, config::prayerEnhanceMessage, config::prayerEnhanceColor, 0),
                new TriggerRule("Thrall", MSG_THRALL_RETURNS, config::enableThrall, config::thrallMessage, config::thrallColor, 0),
                new TriggerRule("Mark of Darkness", MSG_MARK_OF_DARKNESS_FADED, config::enableMarkOfDarkness, config::markOfDarknessMessage, config::markOfDarknessColor, 0),
                new TriggerRule("Saturated Heart", MSG_SATURATED_HEART_WORN_OFF, config::enableSaturatedHeart, config::saturatedHeartMessage, config::saturatedHeartColor, 0),
                new TriggerRule("Stamina", MSG_STAMINA_EXPIRED, config::enableStamina, config::staminaMessage, config::staminaColor, 0),
                new TriggerRule("Soulflamehorn", MSG_SOULFLAMEHORN_1, config::enableSoulflameHorn, config::soulflameHornMessage, config::soulflameHornColor, 6),
                new TriggerRule("Soulflamehorn", MSG_SOULFLAMEHORN_2, config::enableSoulflameHorn, config::soulflameHornMessage, config::soulflameHornColor, 6),
                new TriggerRule("Surge", MSG_SURGE_POTION_READY, config::enableSurgePotion, config::surgePotionMessage, config::surgePotionColor, 0)
        );
    }

    @Override
    protected void shutDown()
    {
        log.info("Overhead Notification Plugin stopped!");
        timer = null;
        triggerRules.clear();
    }

    @Subscribe
    public void onChatMessage(ChatMessage event)
    {
        if (event.getType() != ChatMessageType.GAMEMESSAGE)
        {
            return;
        }

        final String msg = event.getMessage();

        if (config.enableDeathCharge() && (msg.matches(DEATH_CHARGE_ACTIVE.pattern()) || msg.matches(UPGRADED_DEATH_CHARGE_ACTIVE.pattern())))
        {
            timer.start();
            return;
        }

        for (TriggerRule rule : triggerRules)
        {
            if (rule.isEnabled.getAsBoolean() && rule.matches(msg))
            {
                triggerAlert(
                        rule.triggerKey,
                        rule.messageSupplier.get(),
                        rule.colorSupplier.get(),
                        rule.durationOverride
                );
                return;
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTick e)
    {
        switch (this.timer.getState()) {
            case EXPIRED:
                triggerAlert("Death Charge", config.deathchargeMessage(), config.deathchargeColor(), 0);
                timer.stop();
                break;
            case IDLE:
            default:
                break;
        }
    }

    @Subscribe
    public void onStatChanged(StatChanged event) {
        if (!"Soulflamehorn".equals(lastTrigger)) {
            return;
        }

        Skill skill = event.getSkill();

        if (skill == Skill.ATTACK || skill == Skill.STRENGTH || skill == Skill.DEFENCE){
            triggerAlert("Clear","", Color.BLACK,0);
        }
    }

    private void triggerAlert(String triggerKey, String message, java.awt.Color color, int duration)
    {
        lastTrigger = triggerKey;
        overhead.show(
                message,
                new String[] { triggerKey },
                color,
                duration > 0 ? duration : config.overheadDisplayTime()
        );
    }

    @Provides
    OverheadNotificationsConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(OverheadNotificationsConfig.class);
    }
}
