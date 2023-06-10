/*
 * The MIT License (MIT)
 *
 * Copyright (c) [YEAR] Jamalam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.jamalam360.tutorial.lib;

import io.github.jamalam360.tutorial.lib.stage.EquipItemStage;
import io.github.jamalam360.tutorial.lib.stage.ObtainAdvancementStage;
import io.github.jamalam360.tutorial.lib.stage.ObtainItemStage;
import io.github.jamalam360.tutorial.lib.stage.UseItemStage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TutorialLibTestMod implements ClientModInitializer {
        public static final String MOD_ID = "tutorial-lib-test-mod";

        @Override
        public void onInitializeClient() {
                Identifier tex = idOf("textures/gui/stages.png");

                Tutorial testTutorial = new Tutorial(
                                new ObtainItemStage(Items.WOODEN_SWORD, new CustomTutorialToast(tex,
                                                Text.translatable("tutorial.testmod.stage_one.title"),
                                                Text.translatable("tutorial.testmod.stage_one.description"))),
                                new ObtainItemStage(Items.LEATHER_HELMET,
                                                new CustomTutorialToast(tex, 20, 0,
                                                                Text.translatable("tutorial.testmod.stage_two.title"),
                                                                Text.translatable(
                                                                                "tutorial.testmod.stage_two.description"))),
                                new EquipItemStage(Items.LEATHER_HELMET,
                                                new CustomTutorialToast(tex, 40, 0,
                                                                Text.translatable("tutorial.testmod.stage_three.title"),
                                                                Text.translatable(
                                                                                "tutorial.testmod.stage_three.description"))),
                                new ObtainItemStage(Items.POTION, new CustomTutorialToast(tex, 60, 0,
                                                Text.translatable("tutorial.testmod.stage_four.title"))),
                                new UseItemStage(Items.POTION,
                                                new CustomTutorialToast(tex, 80, 0,
                                                                Text.translatable("tutorial.testmod.stage_five.title"),
                                                                Text.translatable(
                                                                                "tutorial.testmod.stage_five.description"))),
                                new ObtainAdvancementStage(new Identifier("adventure/kill_a_mob"),
                                                new CustomTutorialToast(tex, 100, 0,
                                                                Text.literal("Nice work!"),
                                                                Text.literal("Tutorial Complete")
                                                                                .styled(s -> s.withItalic(true)))));

                Registry.register(TutorialLib.TUTORIAL_REGISTRY, TutorialLib.idOf("test_tutorial"), testTutorial);

                ClientCommandRegistrationCallback.EVENT.register((dispatcher, r) -> {
                        dispatcher.register(ClientCommandManager.literal("reset_test_tutorial").executes(context -> {
                                testTutorial.setCurrentStageIndex(0);
                                return 0;
                        }));
                });
        }

        public static Identifier idOf(String path) {
                return new Identifier(MOD_ID, path);
        }
}
