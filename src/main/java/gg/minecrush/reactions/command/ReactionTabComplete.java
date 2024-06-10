package gg.minecrush.reactions.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.ArrayList;
import java.util.List;

public class ReactionTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<String>();
        if (args.length == 1) {
            list.add("start");
            list.add("stop");
            list.add("remove");
            list.add("add");
            list.add("reload");
            list.add("list");
            return list;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
            List<String> completions = new ArrayList<>();
            completions.add("math");
            completions.add("scramble");
            completions.add("fastest");
            return completions;
        }
        return null;
    } // To do: Add tab complete for "Remove" (To show all the words already in the list)
}
