package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.council.CouncilEngine;
import com.narxoz.rpg.council.CouncilRunResult;
import com.narxoz.rpg.guild.Captain;
import com.narxoz.rpg.guild.GuildHall;
import com.narxoz.rpg.guild.Healer;
import com.narxoz.rpg.guild.Loremaster;
import com.narxoz.rpg.guild.Quartermaster;
import com.narxoz.rpg.guild.Scout;
import com.narxoz.rpg.quest.Quest;
import com.narxoz.rpg.quest.QuestLog;
import com.narxoz.rpg.quest.QuestPriority;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for Homework 10 — The Adventurers' Guild: Iterator + Mediator.
 *
 * The scaffold prints the banner only; students fill in the guild demo.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Homework 10 Demo: Iterator + Mediator ===");

        Hero elara = new Hero("Elara", 110, 40, 16, 9, 120);
        Hero kael = new Hero("Kael", 95, 25, 22, 6, 45);

        List<Hero> party = new ArrayList<>();
        party.add(elara);
        party.add(kael);

        QuestLog questLog = new QuestLog();
        questLog.add(new Quest("Rat cellar sweep", QuestPriority.LOW, 25, false));
        questLog.add(new Quest("Escort the merchant caravan", QuestPriority.NORMAL, 80, false));
        questLog.add(new Quest("Cursed barrow excavation", QuestPriority.HIGH, 200, false));
        questLog.add(new Quest("Dragon ridge beacon", QuestPriority.URGENT, 350, true));
        questLog.add(new Quest("Supply run to the northern pass", QuestPriority.NORMAL, 55, false));

        GuildHall hall = new GuildHall();
        new Quartermaster("Mira", hall);
        new Scout("Joric", hall);
        new Healer("Senna", hall);
        new Captain("Alden", hall);
        new Loremaster("Archivist Thalen", hall);

        CouncilEngine engine = new CouncilEngine();
        CouncilRunResult result = engine.runCouncil(party, questLog, hall);

        System.out.println("\n=== Final council result ===");
        System.out.println(result);
    }
}
