package com.narxoz.rpg.council;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.guild.Captain;
import com.narxoz.rpg.guild.GuildHall;
import com.narxoz.rpg.guild.GuildMediator;
import com.narxoz.rpg.guild.Healer;
import com.narxoz.rpg.guild.Loremaster;
import com.narxoz.rpg.guild.Quartermaster;
import com.narxoz.rpg.guild.Scout;
import com.narxoz.rpg.quest.Quest;
import com.narxoz.rpg.quest.QuestIterator;
import com.narxoz.rpg.quest.QuestLog;
import com.narxoz.rpg.quest.QuestPriority;
import java.util.List;

/**
 * Orchestrates a planning session that uses both Iterator and Mediator.
 */
public class CouncilEngine {

    public CouncilRunResult runCouncil(List<Hero> party, QuestLog questLog, GuildMediator hall) {
        GuildHall guildHall = hall instanceof GuildHall ? (GuildHall) hall : null;
        if (guildHall != null) {
            guildHall.resetRoutingStats();
        }

        System.out.println("\n=== War council ===");
        if (party != null && !party.isEmpty()) {
            System.out.println("[Council] Heroes:");
            for (Hero hero : party) {
                System.out.println("  " + hero);
            }
        }

        int questsTraversed = 0;

        if (questLog != null && guildHall != null) {
            Captain captain = guildHall.getCaptain();
            Scout scout = guildHall.getScout();
            Quartermaster quartermaster = guildHall.getQuartermaster();
            Healer healer = guildHall.getHealer();
            Loremaster loremaster = guildHall.getLoremaster();

            System.out.println("\n[Iterator] Pass 1 — arrival order (OrderedQuestIterator)");
            QuestIterator ordered = questLog.ordered();
            while (ordered.hasNext()) {
                Quest quest = ordered.next();
                questsTraversed++;
                System.out.println("  -> " + quest);
                if (captain != null) {
                    captain.issueOrder("mission", "Briefing for: " + quest.getTitle()
                            + " | priority=" + quest.getPriority()
                            + " | reward=" + quest.getRewardGold() + "g");
                }
            }

            System.out.println("\n[Iterator] Pass 2 — newest first (ReverseQuestIterator)");
            QuestIterator reverse = questLog.reverse();
            while (reverse.hasNext()) {
                Quest quest = reverse.next();
                questsTraversed++;
                System.out.println("  -> " + quest);
                if (scout != null) {
                    scout.reportRoute("recon", "Route intel update for: " + quest.getTitle());
                }
            }

            System.out.println("\n[Iterator] Pass 3 — priority >= HIGH (PriorityQuestIterator)");
            QuestIterator priority = questLog.priorityAtLeast(QuestPriority.HIGH);
            while (priority.hasNext()) {
                Quest quest = priority.next();
                questsTraversed++;
                System.out.println("  -> " + quest);
                if (healer != null) {
                    healer.prepareAid("medical", "Casualty prep for dangerous contract: " + quest.getTitle());
                }
            }

            System.out.println("\n[Iterator] Pass 4 — reward order (RewardSortedQuestIterator)");
            QuestIterator byReward = questLog.rewardSorted();
            while (byReward.hasNext()) {
                Quest quest = byReward.next();
                questsTraversed++;
                System.out.println("  -> " + quest);
                if (quartermaster != null) {
                    quartermaster.requestSupplies("rewards", "Payout planning for: " + quest.getTitle()
                            + " (" + quest.getRewardGold() + "g)");
                }
                if (loremaster != null && quest.isUrgent()) {
                    loremaster.shareLore("lore", "Chronicle note for urgent line: " + quest.getTitle());
                }
            }
        }

        int messagesRouted = guildHall != null ? guildHall.getRoutesDispatched() : 0;
        int membersNotified = guildHall != null ? guildHall.getReceivesDelivered() : 0;

        System.out.println("\n[Council] Summary: quests visited=" + questsTraversed
                + ", dispatches=" + messagesRouted
                + ", colleague receives=" + membersNotified);

        return new CouncilRunResult(questsTraversed, messagesRouted, membersNotified);
    }
}
