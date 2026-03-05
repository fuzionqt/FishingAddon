package com.FishingAddon.mixins;

import com.FishingAddon.module.QOL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class InactivityFpsLimiterMixin {

  @Shadow
  public Options options;

  @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
  private void fishingAddon$disableInactivityLimit(CallbackInfoReturnable<Integer> cir) {
    if (QOL.INSTANCE.getDisableInactivityFpsLimit()) {
      cir.setReturnValue(this.options.framerateLimit().get());
    }
  }
}
