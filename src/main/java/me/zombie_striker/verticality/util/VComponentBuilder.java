package me.zombie_striker.verticality.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VComponentBuilder {

    private static final Pattern COMPONENT_PATTERN = Pattern.compile("\\{(.*?)\\}");

    /**
     * Converts a chat format string to a Component.
     *
     * @param chatFormat The chat format string.
     * @param group      The player's group name to be included in the message.
     * @param player     The player's name to be included in the message.
     * @param message    The message sent by the player.
     * @return A Component representing the formatted chat message.
     */
    public static Component buildChatMessage(String chatFormat, String group, String player, String message) {
        String formattedString = chatFormat
                .replace("{group}", group)
                .replace("{player}", player)
                .replace("{message}", message);

        return parseComponents(formattedString);
    }

    /**
     * Parses components from the string.
     *
     * @param message The message string with component definitions.
     * @return A Component built from the message with applied components.
     */
    private static Component parseComponents(String message) {
        Component component = Component.text("");
        Matcher matcher = COMPONENT_PATTERN.matcher(message);
        int lastEnd = 0;

        while (matcher.find()) {
            String textBeforeComponent = message.substring(lastEnd, matcher.start());
            if (!textBeforeComponent.isEmpty()) {
                component = component.append(Component.text(textBeforeComponent));
            }

            String componentData = matcher.group(1);
            Component parsedComponent = parseComponentData(componentData);
            component = component.append(parsedComponent);
            lastEnd = matcher.end();
        }
        // Append any remaining text after last component
        if (lastEnd < message.length()) {
            component = component.append(Component.text(message.substring(lastEnd)));
        }

        return component;
    }

    /**
     * Parses a component data string to create a Component with behavior.
     * <p>
     * Example component data formats:
     * - "color:hex(#ff0000);click:open_url:https://example.com"
     *
     * @param componentData The data that defines the component's behavior.
     * @return A Component with specified formatting and event behavior.
     */
    private static Component parseComponentData(String componentData) {
        Component component = Component.text("Text");

        // Extract properties from the data.
        String[] parts = componentData.split(";");
        for (String part : parts) {
            String[] keyValue = part.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                switch (key) {
                    case "color":
                        if (value.startsWith("hex(") && value.endsWith(")")) {
                            String hexValue = value.substring(4, value.length() - 1);
                            component = component.color(TextColor.fromHexString(hexValue));
                        }
                        break;
                    case "click":
                        if (value.startsWith("open_url:")) {
                            component = component.clickEvent(ClickEvent.openUrl(value.substring(9)));
                        }
                        break;
                    case "hover":
                        if (value.startsWith("show_text:")) {
                            component = component.hoverEvent(HoverEvent.showText(Component.text(value.substring(10))));
                        }
                        break;
                }
            }
        }

        return component;
    }
}
