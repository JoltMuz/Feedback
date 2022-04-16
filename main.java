package io.github.JoltMuz.feedback;

import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Set;

public final class main extends JavaPlugin implements CommandExecutor, Listener
{
    @Override
    public void onEnable()
    {

        this.getCommand("feedback").setExecutor(this);
        getServer().getPluginManager().registerEvents(this,this);

        if(getConfig().getConfigurationSection("Ratings") != null )
        {
            Set<String> set = getConfig().getConfigurationSection("Ratings").getKeys(false);
            for(String rating : set)
            {
                double value = getConfig().getDouble("Ratings."+rating);
                Ratings.put(rating, value);
            }
        }

    }

    @Override
    public void onDisable()
    {
       for(String key : Ratings.keySet())
       {
           double value = Ratings.get(key);
           getConfig().set("Ratings."+key, value);
       }
       saveConfig();


    }
    HashMap<String,Double> Ratings = new HashMap<>();
    HashMap<String,String> VotedPlayers = new HashMap<>();
    HashMap<String,Integer> Voters = new HashMap<>();

    boolean confirmation = false;

    String signature = ChatColor.GOLD + ChatColor.BOLD.toString() + "Feedback" + ChatColor.DARK_GRAY + " 》";
    @Override
    public boolean onCommand(CommandSender Sender, Command command, String alias, String[] args)
    {
        if (Sender.isOp())
        {
            if (args.length == 2 && ( args[0].equalsIgnoreCase("ask") || args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("remove" )))
            {
                if (args[0].equalsIgnoreCase("ask"))
                {
                    if ( ! Ratings.containsKey(args[1]) )
                    {
                        Bukkit.broadcastMessage(signature + ChatColor.YELLOW + "Please rate " + ChatColor.GOLD + args[1] + ChatColor.YELLOW + " out of 5 by clicking:" );

                        Ratings.put(args[1],0.0);
                        Voters.put(args[1],0);

                        TextComponent five = new TextComponent("[5]");
                        five.setColor(net.md_5.bungee.api.ChatColor.DARK_GREEN);
                        five.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/feedback rate " + args[1] + " 5"));
                        five.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Perfect!").color(net.md_5.bungee.api.ChatColor.DARK_GREEN).create()));

                        TextComponent four = new TextComponent("[4]");
                        four.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                        four.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/feedback rate " + args[1] + " 4"));
                        four.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Great!").color(net.md_5.bungee.api.ChatColor.GREEN).create()));

                        TextComponent three = new TextComponent("[3]");
                        three.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
                        three.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/feedback rate " + args[1] + " 3"));
                        three.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Good").color(net.md_5.bungee.api.ChatColor.YELLOW).create()));

                        TextComponent two = new TextComponent("[2]");
                        two.setColor(net.md_5.bungee.api.ChatColor.GOLD);
                        two.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/feedback rate " + args[1] + " 2"));
                        two.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Okay").color(net.md_5.bungee.api.ChatColor.GOLD).create()));

                        TextComponent one = new TextComponent("[1]");
                        one.setColor(net.md_5.bungee.api.ChatColor.RED);
                        one.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/feedback rate " + args[1] + " 1"));
                        one.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Bad").color(net.md_5.bungee.api.ChatColor.RED).create()));

                        for(Player p : Bukkit.getOnlinePlayers())
                        {
                            p.spigot().sendMessage(five);
                            p.spigot().sendMessage(four);
                            p.spigot().sendMessage(three);
                            p.spigot().sendMessage(two);
                            p.spigot().sendMessage(one);
                        }

                    }
                    else
                    {
                        Sender.sendMessage(signature + ChatColor.RED + "You already asked feedback for " + ChatColor.RED + args[1]);
                    }
                }
                if (args[0].equalsIgnoreCase("check"))
                {
                    if ( ! Ratings.containsKey(args[1]) )
                    {
                        Sender.sendMessage(signature + ChatColor.RED + "You first need to ask feedback for " + ChatColor.DARK_RED + args[1] + ChatColor.RED + ".");
                    }
                    else
                    {
                        String coloredRate = Ratings.get(args[1]) + " ";
                        if (Ratings.get(args[1]) == 5)
                        {
                            coloredRate = ChatColor.DARK_GREEN.toString() + Ratings.get(args[1]);
                        }
                        else if (Ratings.get(args[1]) >= 4)
                        {
                            coloredRate = ChatColor.GREEN.toString() + Ratings.get(args[1]);
                        }
                        else if (Ratings.get(args[1]) >= 3)
                        {
                            coloredRate = ChatColor.YELLOW.toString() + Ratings.get(args[1]);
                        }
                        else if (Ratings.get(args[1]) >= 2)
                        {
                            coloredRate = ChatColor.GOLD.toString() + Ratings.get(args[1]);
                        }
                        else if (Ratings.get(args[1]) > 0)
                        {
                            coloredRate = ChatColor.RED.toString() + Ratings.get(args[1]);
                        }
                        Sender.sendMessage(signature + ChatColor.YELLOW + "Average ratings for " + ChatColor.AQUA + args[1] + ChatColor.YELLOW + ": " + coloredRate);
                    }
                }
                if (args[0].equalsIgnoreCase("remove"))
                {
                    if ( Ratings.containsKey(args[1]) )
                    {
                        Voters.remove(args[1]);
                        Ratings.remove(args[1]);
                        getConfig().set("Ratings."+args[1], null);
                        Sender.sendMessage(signature + ChatColor.GREEN + "Feedback ratings removed for " + ChatColor.DARK_GREEN + args[1] + ChatColor.GREEN + ".");
                    }
                    else
                    {
                        Sender.sendMessage(signature + ChatColor.RED + "Feedback for " + ChatColor.DARK_RED + args[1] + ChatColor.RED + " does not exist." );
                    }
                }
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("list"))
            {
                Sender.sendMessage(signature + ChatColor.GOLD + "Feedbacks List: ");
                for (String title : Ratings.keySet())
                {
                    String coloredRate = ChatColor.AQUA.toString() + Ratings.get(title) + " ";
                    if (Ratings.get(title) == 5)
                    {
                        coloredRate = ChatColor.DARK_GREEN.toString() + Ratings.get(title);
                    }
                    else if (Ratings.get(title) >= 4)
                    {
                        coloredRate = ChatColor.GREEN.toString() + Ratings.get(title);
                    }
                    else if (Ratings.get(title) >= 3)
                    {
                        coloredRate = ChatColor.YELLOW.toString() + Ratings.get(title);
                    }
                    else if (Ratings.get(title) >= 2)
                    {
                        coloredRate = ChatColor.GOLD.toString() + Ratings.get(title);
                    }
                    else if (Ratings.get(title) > 0)
                    {
                        coloredRate = ChatColor.RED.toString() + Ratings.get(title);
                    }
                    Sender.sendMessage(signature + ChatColor.AQUA + title + ChatColor.YELLOW + " : " + coloredRate);
                }
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("clearall"))
            {
                if (confirmation)
                {
                    Ratings.clear();
                    VotedPlayers.clear();
                    Voters.clear();
                    getConfig().set("Ratings",null);
                    confirmation = false;
                    Sender.sendMessage(signature + ChatColor.GREEN  + "All ratings for all titles cleared.");
                }
                else
                {
                    Sender.sendMessage(signature + ChatColor.YELLOW  + "Type the command again in 5 seconds to" + ChatColor.RED + " clear all Ratings.");
                    confirmation = true;
                    getServer().getScheduler().scheduleSyncDelayedTask(this, () -> confirmation = false, 100L);
                }
            }
            else
            {
                Sender.sendMessage(signature + ChatColor.RED + "OP Usage: /feedback [ask|check|remove] [Title]");
            }
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("rate") && Sender instanceof Player)
        {
            if (Ratings.containsKey(args[1]))
            {
                if (VotedPlayers.containsKey(Sender.getName()) && VotedPlayers.get(Sender.getName()).equalsIgnoreCase(args[1]))
                {
                    Sender.sendMessage(signature + ChatColor.DARK_GRAY + "You've already voted!");
                }
                else
                {
                    Voters.put(args[1], Voters.get(args[1]) + 1);
                    VotedPlayers.put(Sender.getName(),args[1]);
                    Sender.sendMessage(signature + ChatColor.YELLOW + "Thanks for your feedback!");

                    if (args[2].equals("5"))
                    {
                        Ratings.put(args[1],(Math.round(Ratings.get(args[1]) + 5 ) / (double) Voters.get(args[1])));
                    }
                    if (args[2].equals("4"))
                    {
                        Ratings.put(args[1],(Math.round(Ratings.get(args[1]) + 4 ) / (double) Voters.get(args[1])));
                    }
                    if (args[2].equals("3"))
                    {
                        Ratings.put(args[1],(Math.round(Ratings.get(args[1]) + 3 ) / (double) Voters.get(args[1])));
                    }
                    if (args[2].equals("2"))
                    {
                        Ratings.put(args[1],(Math.round(Ratings.get(args[1]) + 2 ) / (double) Voters.get(args[1])));
                    }
                    if (args[2].equals("1"))
                    {
                        Ratings.put(args[1],(Math.round(Ratings.get(args[1]) + 1 ) / (double) Voters.get(args[1])));
                    }
                }
            }
            else
            {
                Sender.sendMessage(signature + ChatColor.DARK_GRAY + "That rating does not exist!");
            }
        }
        else
        {
            Sender.sendMessage(signature + ChatColor.RED + "This command requires OP");
        }
        return true;
    }
}
