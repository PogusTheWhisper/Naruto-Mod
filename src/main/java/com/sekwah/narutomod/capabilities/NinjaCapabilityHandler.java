package com.sekwah.narutomod.capabilities;

import com.sekwah.narutomod.NarutoMod;
import com.sekwah.sekclib.SekCLib;
import com.sekwah.sekclib.api.capability.CapabilitySyncRegistry;
import com.sekwah.sekclib.capabilitysync.CapabilityEntry;
import com.sekwah.sekclib.capabilitysync.SyncEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NarutoMod.MOD_ID)
public class NinjaCapabilityHandler {

    @CapabilityInject(INinjaData.class)
    public static final Capability<INinjaData> NINJA_DATA = null;

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(NarutoMod.MOD_ID, "ninja_data"), new NinjaData());
        }
    }

    @SubscribeEvent
    public static void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
        if(event.side.isServer()) {
            Player player = event.player;
            player.getCapability(NINJA_DATA).ifPresent(data -> {
                data.setChakra(data.getChakra() + 1);
            });
        }
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        // TODO need to implement copying data
        event.getOriginal().getCapability(NINJA_DATA).ifPresent(original -> {
            event.getPlayer().getCapability(NINJA_DATA).ifPresent(future -> {
                future.deserializeNBT(original.serializeNBT());
            });
        });
    }

    /**
     * Server side event
     * @param event
     */
    @SubscribeEvent
    public static void playerTracking(PlayerEvent.StartTracking event) {
        // TODO triggers on any entity. trigger on players send over the original data
    }

    //public static final EntitySize STANDING_SIZE = EntitySize.flexible(0.1F, 0.1F);

    // For things like the transformation jutsus (make sure to trigger it manually when you change)
    @SubscribeEvent
    public static void playerSize(EntityEvent.Size event) {
//        Entity entity = event.getEntity();
//        if (entity instanceof PlayerEntity) {
//            event.setNewEyeHeight(5f);
//            event.setNewSize(EntitySize.flexible(10F, 10F));
//        }
    }
}
