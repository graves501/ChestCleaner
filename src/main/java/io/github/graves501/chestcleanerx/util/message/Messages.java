package io.github.graves501.chestcleanerx.util.message;

public class Messages {

    private Messages() {
    }

//    private static List<String> messageList = new ArrayList<>();
//
//    public static void setMessageList(final List<String> messagesLoadedFromConfig) {
//
//        final PluginConfiguration pluginConfiguration = PluginConfiguration.getInstance();
//        final List<String> defaultMessages = getDefaultMessages();
//
//        if (messagesLoadedFromConfig == null) {
//            pluginConfiguration.setAndSaveMessageList(getDefaultMessages());
//        } else if (messagesLoadedFromConfig.size() >= defaultMessages.size()) {
//            messageList = messagesLoadedFromConfig;
//        } else {
//            messageList = messagesLoadedFromConfig;
//            messageList.addAll(defaultMessages);
//
//            pluginConfiguration.setAndSaveMessageList(messageList);
//        }
//
//    }
//
//    private static List<String> getDefaultMessages() {
//        final List<String> defaultMessages = new ArrayList<>();
//
//        defaultMessages.add("Syntax Error");
//        defaultMessages.add("Error");
//        defaultMessages.add("I'm sorry, but you do not have permission to perform this command.");
//        defaultMessages.add("You can sort the next inventory in %time seconds.");
//        defaultMessages.add("The block at the location %location has no inventory.");
//        defaultMessages.add("Inventory sorted.");
//        defaultMessages.add("There is no world with the name \"%worldname\".");
//        defaultMessages.add("You have to be a player to perform this Command");
//        defaultMessages.add("Timer: true");
//        defaultMessages.add("Timer: false");
//        defaultMessages.add("Timer: %time");
//        defaultMessages.add("The name of your new cleaning item is now: \"%itemname�a\"");
//        defaultMessages.add("Cleaning item lore was set.");
//        defaultMessages.add("%newitem is now the new cleaning item.");
//        defaultMessages.add("You have to hold an item to do this.");
//        defaultMessages.add("You got a cleaning item.");
//        defaultMessages.add("Cleaning item: true");
//        defaultMessages.add("Cleaning item: false");
//        defaultMessages.add("The player %playername got a cleaning item.");
//        defaultMessages.add("Player %playername is not online.");
//        defaultMessages.add("OpenInventoryEvent-DetectionMode was set to: %modeBoolean");
//        defaultMessages.add(
//            "A new update is available at:�b https://www.spigotmc.org/resources/40313/updates");
//        defaultMessages.add("DurabilityLoss: true");
//        defaultMessages.add("DurabilityLoss: false");
//        defaultMessages.add("The material of the item \"%material\" was added to the blacklist.");
//        defaultMessages.add("The material \"%material\" was removed form the blacklist.");
//        defaultMessages.add("The blacklist does not contain the material \"%material\".");
//        defaultMessages.add(
//            "Index is out of bounds, it have to be bigger than -1 and smaller than %biggestindex.");
//        defaultMessages.add("The blacklist is empty.");
//        defaultMessages.add("The BlackList page %page:");
//        defaultMessages.add("For the next entries: /list %nextpage");
//        defaultMessages.add("Invalid input for an integer: %index");
//        defaultMessages.add("Invalid page number (valid  page number range: %range)");
//        defaultMessages.add("There is no material with the name \"%material\".");
//        defaultMessages.add("The blacklist was successfully cleared.");
//        defaultMessages.add("The material %material is already on the blacklist.");
//        defaultMessages.add("This inventory is on the blacklist, you can't sort it.");
//        defaultMessages.add("There is no pattern with this id.");
//        defaultMessages.add("Pattern was set.");
//        defaultMessages.add("There is no evaluator with this id.");
//        defaultMessages.add("Evaluator was set.");
//        defaultMessages.add("Autosorting was set to %boolean.");
//        defaultMessages.add("New default sorting pattern was set.");
//        defaultMessages.add("New default evaluator was set.");
//        defaultMessages.add("Default auto sorting was set to %boolean.");
//
//        return defaultMessages;
//    }
//
//    public static String getMessage(InGameMessage inGameMessage) {
//        return inGameMessage.get();
//    }
//
//    public static String getMessage(InGameMessage inGameMessage, String target, String replacement) {
//        String messageToBeSent = inGameMessage.get();
//        messageToBeSent = messageToBeSent.replace(target, replacement);
//
//        return messageToBeSent;
//    }

}
