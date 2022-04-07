package io.github.JoltMuz.feedback;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class main extends JavaPlugin implements CommandExecutor, Listener
{
    @Override
    public void onEnable()
    {

        this.getCommand("feedback").setExecutor(this);
        getServer().getPluginManager().registerEvents(this,this);

    }

    @Override
    public void onDisable()
    {

    }
    HashMap<String,Double> Ratings = new HashMap<>();
    HashMap<String,String> VotedPlayers = new HashMap<>();
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
                        Bukkit.broadcastMessage(signature + ChatColor.YELLOW + "Please rate " + ChatColor.GOLD + args[1] + ChatColor.YELLOW + " out of 5." );
                        for(Player p : Bukkit.getOnlinePlayers())
                        {
                            Ratings.put(args[1],0.0);

                            TextComponent five = new TextComponent("[5]");
                            five.setColor(net.md_5.bungee.api.ChatColor.DARK_GREEN);
                            five.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/feedback rate " + args[1] + " 5"));
                            p.spigot().sendMessage(five);
                            TextComponent four = new TextComponent("[4]");
                            four.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                            four.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/feedback rate " + args[1] + " 4"));
                            p.spigot().sendMessage(four);
                            TextComponent three = new TextComponent("[3]");
                            three.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
                            three.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/feedback rate " + args[1] + " 3"));
                            p.spigot().sendMessage(three);
                            TextComponent two = new TextComponent("[2]");
                            two.setColor(net.md_5.bungee.api.ChatColor.GOLD);
                            two.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/feedback rate " + args[1] + " 2"));
                            p.spigot().sendMessage(two);
                            TextComponent one = new TextComponent("[1]");
                            one.setColor(net.md_5.bungee.api.ChatColor.RED);
                            one.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/feedback rate " + args[1] + " 1"));
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
                        Sender.sendMessage(signature + ChatColor.YELLOW + "Average ratings for " + ChatColor.GOLD + args[1] + ChatColor.YELLOW + ": " + coloredRate);
                    }
                }
                if (args[0].equalsIgnoreCase("remove"))
                {
                    if ( Ratings.containsKey(args[1]) )
                    {
                        Ratings.remove(args[1]);
                        Sender.sendMessage(signature + ChatColor.GREEN + "Feedback ratings removed for " + ChatColor.DARK_GREEN + args[1] + ChatColor.GREEN + ".");
                    }
                    else
                    {
                        Sender.sendMessage(signature + ChatColor.RED + "Feedback for " + ChatColor.DARK_RED + args[1] + ChatColor.RED + " does not exist." );
                    }
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
                    if (args[2].equals("5"))
                    {
                        VotedPlayers.put(Sender.getName(),args[1]);
                        Ratings.put(args[1],(Ratings.get(args[1]) + 5 ) / (double) VotedPlayers.size());
                        Sender.sendMessage(signature + ChatColor.YELLOW + "Thanks for your feedback!");
                    }
                    if (args[2].equals("4"))
                    {
                        VotedPlayers.put(Sender.getName(), args[1]);
                        Ratings.put(args[1],(Ratings.get(args[1]) + 4 ) / (double) VotedPlayers.size());
                        Sender.sendMessage(signature + ChatColor.YELLOW + "Thanks for your feedback!");
                    }
                    if (args[2].equals("3"))
                    {
                        VotedPlayers.put(Sender.getName(),args[1]);
                        Ratings.put(args[1],(Ratings.get(args[1]) + 3 ) / (double) VotedPlayers.size());
                        Sender.sendMessage(signature + ChatColor.YELLOW + "Thanks for your feedback!");
                    }
                    if (args[2].equals("2"))
                    {
                        VotedPlayers.put(Sender.getName(),args[1]);
                        Ratings.put(args[1],(Ratings.get(args[1]) + 2 ) / (double) VotedPlayers.size());
                        Sender.sendMessage(signature + ChatColor.YELLOW + "Thanks for your feedback!");
                    }
                    if (args[2].equals("1"))
                    {
                        VotedPlayers.put(Sender.getName(),args[1]);
                        Ratings.put(args[1],(Ratings.get(args[1]) + 1 ) / (double) VotedPlayers.size());
                        Sender.sendMessage(signature + ChatColor.YELLOW + "Thanks for your feedback!");
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
