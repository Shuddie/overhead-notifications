
package com.shuddie.overhead;
/*
 * Copyright (c) 2025, Shuddie <http://github.com/Shuddie>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.client.Notifier;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
        name = "Overhead Notification",
        description = "Shows overhead text when boosts expire",
        tags = {"overhead", "notification", "chat", "thrall", "overload", "boost"}
)
public class OverheadNotificationsPlugin extends Plugin
{
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
    public static final Pattern DEATH_CHARGE_ACTIVE =
            Pattern.compile("<col=[A-Fa-f\\d]+>Upon the death of your next foe, some of your special attack energy will be restored\\.</col>");

    public static final Pattern UPGRADED_DEATH_CHARGE_ACTIVE =
            Pattern.compile("<col=[A-Fa-f\\d]+>Upon the death of your next two foes, some of your special attack energy will be restored\\.</col>");

    @Override
    protected void startUp()
    {
        log.info("Overhead Notification Plugin started!");
        timer = new DeathChargeTimer();
    }

    @Override
    protected void shutDown()
    {
        log.info("Overhead Notification Plugin stopped!");
        timer = null;
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
        }
        // ===== Overloads =====
        if (config.enableOverload() && msg.contains("overload have worn off"))
        {
            triggerAlert("Overload", config.overloadMessage(), config.overloadColor());
        }
        else if (config.enableSmellingSalt() && msg.contains("smelling salts has worn off"))
        {
            triggerAlert("Smelling Salt", config.smellingSaltMessage(), config.smellingSaltColor());
        }

        // ===== Divines =====
        else if (config.enableDivinePot() && msg.contains("divine potion have worn off"))
        {
            triggerAlert("Divine Pot", config.divinePotMessage(), config.divinePotColor());
        }

        // ===== Prayer =====
        else if (config.enablePrayerRegen() && msg.contains("prayer regeneration"))
        {
            triggerAlert("Prayer Regen", config.prayerRegenMessage(), config.prayerRegenColor());
        }
        else if (config.enablePrayerEnhance() && msg.contains("prayer enhance effect has worn off"))
        {
            triggerAlert("Prayer Enhance", config.prayerEnhanceMessage(), config.prayerEnhanceColor());
        }

        // ===== Other =====
        else if (config.enableThrall() && msg.contains("thrall returns to the grave"))
        {
            triggerAlert("Thrall", config.thrallMessage(), config.thrallColor());
        }
        else if (config.enableMarkOfDarkness() && msg.contains("Mark of Darkness has faded away"))
        {
            triggerAlert("Mark of Darkness", config.markOfDarknessMessage(), config.markOfDarknessColor());
        }
        else if (config.enableSaturatedHeart() && msg.contains("saturated heart have worn off"))
        {
            triggerAlert("Saturated Heart", config.saturatedHeartMessage(), config.saturatedHeartColor());
        }
        else if (config.enableStamina() && msg.contains("stamina enhancement has expired"))
        {
            triggerAlert("Stamina", config.staminaMessage(), config.staminaColor());
        }
    }
    @Subscribe
    public void onGameTick(GameTick e)
    {
        switch (this.timer.getState()) {
            case EXPIRED:
                triggerAlert("Death Charge", config.deathchargeMessage(), config.deathchargeColor());
                timer.stop();
                break;
            case IDLE:
            default:
                break;
        }
    }
    private void triggerAlert(String triggerKey, String message, java.awt.Color color)
    {
        overhead.show(
                message,
                new String[] { triggerKey },
                color,
                config.overheadDisplayTime()
        );
    }

    @Provides
    OverheadNotificationsConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(OverheadNotificationsConfig.class);
    }
}
