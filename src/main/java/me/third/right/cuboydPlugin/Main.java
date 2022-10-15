package me.third.right.cuboydPlugin;

import me.third.right.commands.Command;
import me.third.right.cuboydPlugin.Hacks.Harass;
import me.third.right.hud.Hud;
import me.third.right.modules.Hack;
import me.third.right.plugins.PluginBase;

@PluginBase.PluginInfo(name = "Cuboyd Harassment", author = "ThirdRight")
public class Main extends PluginBase {

    @Override
    public Hack[] registerHacks() {
        return new Hack[] {new Harass()};
    }

    @Override
    public Hud[] registerHuds() {
        return new Hud[0];
    }

    @Override
    public Command[] registerCommands() {
        return new Command[0];
    }
}
