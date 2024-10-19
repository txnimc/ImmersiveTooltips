package toni.immersivemessages;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import toni.immersivemessages.api.ImmersiveMessage;

#if FORGE
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
#endif

#if NEO
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
#endif

public class IMClient {

    #if FORGE
        public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ImmersiveMessages.ID);

        public static final RegistryObject<SoundEvent> LOW = SOUNDS.register("vocalsynthlow", () -> SoundEvent.createVariableRangeEvent(#if AFTER_21_1 ResourceLocation.fromNamespaceAndPath #else new ResourceLocation #endif(ImmersiveMessages.ID, "vocalsynthlow")));
        public static final RegistryObject<SoundEvent> LOWSHORT = SOUNDS.register("vocalsynthlowshort", () -> SoundEvent.createVariableRangeEvent(#if AFTER_21_1 ResourceLocation.fromNamespaceAndPath #else new ResourceLocation #endif(ImmersiveMessages.ID, "vocalsynthlowshort")));
    #elif NEO
        public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ImmersiveMessages.ID);

        public static final DeferredHolder<SoundEvent, SoundEvent> LOW = SOUNDS.register("vocalsynthlow", () -> SoundEvent.createVariableRangeEvent(#if AFTER_21_1 ResourceLocation.fromNamespaceAndPath #else new ResourceLocation #endif(ImmersiveMessages.ID, "vocalsynthlow")));
        public static final DeferredHolder<SoundEvent, SoundEvent> LOWSHORT = SOUNDS.register("vocalsynthlowshort", () -> SoundEvent.createVariableRangeEvent(#if AFTER_21_1 ResourceLocation.fromNamespaceAndPath #else new ResourceLocation #endif(ImmersiveMessages.ID, "vocalsynthlowshort")));

    #else
        //#if NEO private static boolean unfrozen = unfreeze(); #endif
    public static final SoundEvent LOW = Registry.register(BuiltInRegistries.SOUND_EVENT, "vocalsynthlow", SoundEvent.createVariableRangeEvent(#if AFTER_21_1 ResourceLocation.fromNamespaceAndPath #else new ResourceLocation #endif(ImmersiveMessages.ID, "vocalsynthlow")));
    public static final SoundEvent LOWSHORT = Registry.register(BuiltInRegistries.SOUND_EVENT, "vocalsynthlowshort", SoundEvent.createVariableRangeEvent(#if AFTER_21_1 ResourceLocation.fromNamespaceAndPath #else new ResourceLocation #endif(ImmersiveMessages.ID, "vocalsynthlowshort")));
        //#if NEO private static boolean frozen = refreeze(); #endif
    #endif

    public static void init() {

    }
}
