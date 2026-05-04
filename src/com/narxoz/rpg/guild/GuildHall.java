package com.narxoz.rpg.guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Topic-based mediator for the Adventurers' Guild war council.
 */
public class GuildHall implements GuildMediator {

    private final Map<String, List<GuildMember>> membersByTopic = new HashMap<>();

    private Captain captain;
    private Scout scout;
    private Quartermaster quartermaster;
    private Healer healer;
    private Loremaster loremaster;

    private int routesDispatched;
    private int receivesDelivered;

    public void resetRoutingStats() {
        routesDispatched = 0;
        receivesDelivered = 0;
    }

    public int getRoutesDispatched() {
        return routesDispatched;
    }

    public int getReceivesDelivered() {
        return receivesDelivered;
    }

    public Captain getCaptain() {
        return captain;
    }

    public Scout getScout() {
        return scout;
    }

    public Quartermaster getQuartermaster() {
        return quartermaster;
    }

    public Healer getHealer() {
        return healer;
    }

    public Loremaster getLoremaster() {
        return loremaster;
    }

    @Override
    public void register(GuildMember member) {
        if (member instanceof Quartermaster) {
            quartermaster = (Quartermaster) member;
            addSubscriber("supplies", member);
            addSubscriber("rewards", member);
            addSubscriber("mission", member);
        } else if (member instanceof Scout) {
            scout = (Scout) member;
            addSubscriber("recon", member);
            addSubscriber("mission", member);
        } else if (member instanceof Healer) {
            healer = (Healer) member;
            addSubscriber("medical", member);
            addSubscriber("mission", member);
        } else if (member instanceof Captain) {
            captain = (Captain) member;
            addSubscriber("mission", member);
            addSubscriber("supplies", member);
            addSubscriber("recon", member);
            addSubscriber("medical", member);
            addSubscriber("lore", member);
            addSubscriber("rewards", member);
        } else if (member instanceof Loremaster) {
            loremaster = (Loremaster) member;
            addSubscriber("lore", member);
            addSubscriber("mission", member);
            addSubscriber("recon", member);
        }
    }

    @Override
    public void dispatch(String topic, GuildMember from, String payload) {
        String safeTopic = topic == null ? "" : topic;
        routesDispatched++;
        System.out.println("[GuildHall] dispatch topic='" + safeTopic + "' from=" + from.getName()
                + " payload=" + payload);

        for (GuildMember subscriber : subscribersFor(safeTopic)) {
            if (subscriber == from) {
                continue;
            }
            subscriber.receive(safeTopic, from, payload);
            receivesDelivered++;
        }
    }

    protected void addSubscriber(String topic, GuildMember member) {
        membersByTopic.computeIfAbsent(topic, key -> new ArrayList<>()).add(member);
    }

    protected List<GuildMember> subscribersFor(String topic) {
        return membersByTopic.getOrDefault(topic, List.of());
    }
}
