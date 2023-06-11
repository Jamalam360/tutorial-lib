/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 Jamalam
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

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.toast.TutorialToast;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

/**
 * A copy of {@link TutorialToast} which allows custom textures. Textures must be 256x256.
 */
public class CustomTutorialToast extends TutorialToast implements ToastDuck {

    private final Text title;
    @Nullable
    private final Text description;
    private Toast.Visibility visibility = Toast.Visibility.SHOW;
    private long lastTime;
    private float lastProgress;
    private float progress;
    private final boolean hasProgressBar;
    private final Identifier texture;
    private final int u;
    private final int v;


    public CustomTutorialToast(Identifier texture, Text title) {
        this(texture, title, null);
    }

    public CustomTutorialToast(Identifier texture, int u, int v, Text title) {
        this(texture, u, v, title, null);
    }

    public CustomTutorialToast(Identifier texture, Text title, @Nullable Text description) {
        this(texture, title, description, false);
    }

    public CustomTutorialToast(Identifier texture, int u, int v, Text title, @Nullable Text description) {
        this(texture, u, v, title, description, false);
    }

    public CustomTutorialToast(Identifier texture, Text title, @Nullable Text description,
            boolean hasProgressBar) {
        this(texture, 0, 0, title, description, hasProgressBar);
    }

    public CustomTutorialToast(Identifier texture, int u, int v, Text title, @Nullable Text description,
            boolean hasProgressBar) {
        // Type.MOUSE is a dummy placeholder.
        super(Type.MOUSE, title, description, hasProgressBar);

        this.title = title;
        this.description = description;
        this.hasProgressBar = hasProgressBar;
        this.texture = texture;
        this.u = u;
        this.v = v;
    }

    @Override
    public Toast.Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        manager.drawTexture(matrices, 0, 0, 0, 96, this.getWidth(), this.getHeight());

        RenderSystem.setShaderTexture(0, this.texture);
        manager.drawTexture(matrices, 6, 6, this.u, this.v, 20, 20);
        RenderSystem.setShaderTexture(0, TEXTURE);

        if (this.description == null) {
            manager.getGame().textRenderer.draw(matrices, this.title, 30.0F, 12.0F, -11534256);
        } else {
            manager.getGame().textRenderer.draw(matrices, this.title, 30.0F, 7.0F, -11534256);
            manager.getGame().textRenderer.draw(matrices, this.description, 30.0F, 18.0F, -16777216);
        }

        if (this.hasProgressBar) {
            DrawableHelper.fill(matrices, 3, 28, 157, 29, -1);
            float f = MathHelper.clampedLerp(this.lastProgress, this.progress,
                    (float) (startTime - this.lastTime) / 100.0F);
            int i;
            if (this.progress >= this.lastProgress) {
                i = -16755456;
            } else {
                i = -11206656;
            }

            DrawableHelper.fill(matrices, 3, 28, (int) (3.0F + 154.0F * f), 29, i);
            this.lastProgress = f;
            this.lastTime = startTime;
        }

        return this.visibility;
    }

    public void show() {
        this.visibility = Toast.Visibility.SHOW;
    }

    public void hide() {
        this.visibility = Toast.Visibility.HIDE;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override
    public void setToastVisibility(boolean visible) {
        if (visible) {
            this.show();
        } else {
            this.hide();
        }
    }
}
