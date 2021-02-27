package com.xeraster.supertnt.items;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StarWarsRecord extends ItemRecord {

    private static final Map RECORDS = Maps.newHashMap();
    private final SoundEvent sound;
    private final String displayName;

    public StarWarsRecord(String p_i46742_1_, SoundEvent soundIn) {
        super(p_i46742_1_, soundIn);
        this.displayName = "record." + p_i46742_1_;
        this.setRegistryName(this.displayName);
        this.setUnlocalizedName(this.displayName);
        this.sound = soundIn;
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.MISC);
        StarWarsRecord.RECORDS.put(this.sound, this);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() == Blocks.JUKEBOX && !((Boolean) iblockstate.getValue(BlockJukebox.HAS_RECORD)).booleanValue()) {
            if (!worldIn.isRemote) {
                ItemStack itemstack = player.getHeldItem(hand);

                ((BlockJukebox) Blocks.JUKEBOX).insertRecord(worldIn, pos, iblockstate, itemstack);
                worldIn.playEvent((EntityPlayer) null, 1010, pos, Item.getIdFromItem(this));
                itemstack.shrink(1);
                player.addStat(StatList.RECORD_PLAYED);
            }

            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.PASS;
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List tooltip, ITooltipFlag flagIn) {
        tooltip.add(this.getRecordNameLocal());
    }

    @SideOnly(Side.CLIENT)
    public String getRecordNameLocal() {
        return I18n.translateToLocal(this.displayName);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public static ItemRecord getBySound(SoundEvent soundIn) {
        return (ItemRecord) StarWarsRecord.RECORDS.get(soundIn);
    }

    @SideOnly(Side.CLIENT)
    public SoundEvent getSound() {
        return this.sound;
    }
}
