package com.blamejared.createtweaker.managers;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.components.crafter.MechanicalCraftingRecipe;
import com.simibubi.create.content.contraptions.components.mixer.MixingRecipe;
import com.simibubi.create.content.contraptions.processing.HeatCondition;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;

@ZenRegister
@ZenCodeType.Name("mods.create.MixingManager")
public class MixingManager implements IRecipeManager {
    
    
    @ZenCodeType.Method
    public void addRecipe(String name, String heat, IItemStack output, IIngredient[] itemInputs, @ZenCodeType.Optional IFluidStack[] fluidInputs) {
        
        name = fixRecipeName(name);
        ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
//        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, ""));
    }
    
    @ZenCodeType.Method
    public void addRecipe(String name, String heat, IFluidStack output, IIngredient[] itemInputs, @ZenCodeType.Optional IFluidStack[] fluidInputs) {
        
        name = fixRecipeName(name);
        ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
        ProcessingRecipeBuilder<MixingRecipe> builder = new ProcessingRecipeBuilder<>(((ProcessingRecipeSerializer<MixingRecipe>) AllRecipeTypes.MIXING.serializer).getFactory(), resourceLocation);
        builder.output(output.getInternal());
        builder.withItemIngredients(Arrays.stream(itemInputs).map(IIngredient::asVanillaIngredient).toArray(Ingredient[]::new));
        if(fluidInputs != null) {
            builder.withFluidIngredients(Arrays.stream(fluidInputs).map(iFluidStack -> FluidIngredient.fromFluidStack(iFluidStack.getInternal())).toArray(FluidIngredient[]::new));
        }
        builder.requiresHeat(Arrays.stream(HeatCondition.values())
                .filter(heatEnum -> heat.equalsIgnoreCase(heatEnum.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid heat: \"" + heat + "\" Provided! Valid options are: " + Arrays.toString(HeatCondition.values()))));
        
        MixingRecipe recipe = builder.build();
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, ""));
    }
    
    @Override
    public IRecipeType<MixingRecipe> getRecipeType() {
        
        return AllRecipeTypes.MIXING.getType();
    }
    
}
