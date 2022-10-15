package me.third.right.cuboydPlugin.Hacks;

import me.bush.eventbus.annotation.EventListener;
import me.third.right.ThirdMod;
import me.third.right.events.client.TickEvent;
import me.third.right.modules.Hack;
import me.third.right.modules.HackStandard;
import me.third.right.settings.setting.EnumSetting;
import me.third.right.utils.client.enums.Category;
import me.third.right.utils.client.objects.DelayTimer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

@Hack.HackInfo(name = "HarassCuboyd", description = "Harass Cuboyd cause why not.", category = Category.OTHER)
public class Harass extends HackStandard {
    //Vars
    private enum MessageTypes { PublicOnly, PrivateOnly, Both }
    private String[] messages = null;
    private final DelayTimer timer = new DelayTimer();
    private boolean isOnline = false;

    //Settings
    private final EnumSetting<MessageTypes> messageType = setting(new EnumSetting<>("MessageType", MessageTypes.values(), MessageTypes.PublicOnly));

    //Overrides
    @Override
    public void onEnable() {
        ThirdMod.EVENT_PROCESSOR.subscribe(this);
        fetchMessages();
    }

    @Override
    public void onDisable() {
        ThirdMod.EVENT_PROCESSOR.unsubscribe(this);
        messages = null;
    }

    @Override
    public String setHudInfo() {
        return String.format("%s", isOnline ? "Online" : "Offline");
    }

    //Events

    @EventListener
    public void onTick(TickEvent event) {
        if(nullCheckFull() || mc.getConnection() == null) return;
        final NetHandlerPlayClient connection = mc.player.connection;
        final NetworkPlayerInfo info = connection.getPlayerInfo(UUID.fromString("2685b6a6-b537-4791-b65c-10991e1acd66"));

        if(info == null || messages == null || messages.length <= 0 || info.getGameProfile().getName().isEmpty()) {
            isOnline = false;
            return;
        }

        isOnline = true;

        if(timer.passedMs(16000)) {
            timer.reset();
            switch (messageType.getSelected()) {
                case PublicOnly:
                    mc.player.sendChatMessage(String.format("%s %s ", info.getGameProfile().getName(), messages[(int) (Math.random() * messages.length)]));
                    break;
                case PrivateOnly:
                    mc.player.sendChatMessage(String.format("/msg %s %s ", info.getGameProfile().getName(), messages[(int) (Math.random() * messages.length)]));
                    break;
                case Both:
                    if((int) (Math.random() * 2) == 0) {
                        mc.player.sendChatMessage(String.format("%s %s ", info.getGameProfile().getName(), messages[(int) (Math.random() * messages.length)]));
                    } else {
                        mc.player.sendChatMessage(String.format("/msg %s %s ", info.getGameProfile().getName(), messages[(int) (Math.random() * messages.length)]));
                    }
                    break;
            }
        }
    }

    //Methods

    private void fetchMessages() {
        final ArrayList<String> messages = new ArrayList<>();
        final InputStream stream = this.getClass().getClassLoader().getResourceAsStream("forCuboyd.txt");
        if(stream == null) return;

        final Scanner scanner = new Scanner(stream);
        while(scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if(line.isEmpty()) continue;
            messages.add(line);
        }

        scanner.close();
        this.messages = messages.toArray(new String[0]);
    }
}
