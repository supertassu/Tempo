/*
 * MIT License
 *
 * Copyright (c) 2018 Tassu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.tassu.tempo.cmd;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.val;
import me.tassu.tempo.api.TempoCommand;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

public class MoveCommand extends TempoCommand {

    private ProxyServer server;

    public MoveCommand(ProxyServer server) {
        super(15);
        this.server = server;
    }

    @Override
    protected void run(CommandSource source, String @NonNull [] args) {
        if (args.length != 2) {
            source.sendMessage(TextComponent.of("Usage: /move <player> <server>", TextColor.GRAY));
            return;
        }

        val player = server.getPlayer(args[0]);

        if (!player.isPresent()) {
            source.sendMessage(TextComponent.of("Could not locate player.", TextColor.GRAY));
            return;
        }

        val serverInfo = server.getServer(args[1]);

        if (!serverInfo.isPresent()) {
            source.sendMessage(TextComponent.of("Could not locate server.", TextColor.GRAY));
            return;
        }

        player.get().createConnectionRequest(serverInfo.get()).fireAndForget();

        source.sendMessage(TextComponent.of(player.get().getUsername(), TextColor.WHITE)
                .append(TextComponent.of(" was summoned to "))
                .append(TextComponent.of(serverInfo.get().getServerInfo().getName(), TextColor.GOLD))
                .append(TextComponent.of(". ", TextColor.GRAY))
        );
    }
}