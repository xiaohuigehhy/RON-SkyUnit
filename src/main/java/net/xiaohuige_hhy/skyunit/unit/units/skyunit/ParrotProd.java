//import com.solegendary.reignofnether.ReignOfNether;
//import com.solegendary.reignofnether.building.buildings.placements.ProductionPlacement;
//import com.solegendary.reignofnether.building.production.ProductionItem;
//import com.solegendary.reignofnether.building.production.StartProductionButton;
//import com.solegendary.reignofnether.building.production.StopProductionButton;
//import com.solegendary.reignofnether.resources.ResourceCost;
//import com.solegendary.reignofnether.unit.units.piglins.GruntProd;
//
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.level.Level;
//import net.xiaohuige_hhy.skyunit.SkyUnit;
//
//public class ParrotProd extends ProductionItem {
//
//    public final static String itemName = "Parrot";
//    public final static ResourceCost cost = ResourceCosts.PARROT;
//
//    public ParrotProd() {
//        super(cost);
//        this.onComplete = (Level level, ProductionPlacement placement) -> {
//            if (!level.isClientSide())
//                placement.produceUnit((ServerLevel) level, SkyUnitEntityRegistrar.PARROT_UNIT.get(), placement.ownerName, true);
//        };
//    }
//
//    public String getItemName() {
//        return ParrotProd.itemName;
//    }
//
//    public UnitSpawnButton getPlaceButton() {
//        return new UnitSpawnButton(
//                itemName,
//                ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/mobheads/parrot.png"),
//                List.of(
//                        FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.parrot"), Style.EMPTY.withBold(true)),
//                        FormattedCharSequence.forward("", Style.EMPTY),
//                        FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.parrot.tooltip1"), Style.EMPTY),
//                        FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.parrot.tooltip2"), Style.EMPTY)
//                )
//        );
//    }
//
//    public StartProductionButton getStartButton(ProductionPlacement prodBuilding, Keybinding hotkey) {
//        List<FormattedCharSequence> tooltipLines = new ArrayList<>(List.of(
//            FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.parrot"), Style.EMPTY.withBold(true)),
//            ResourceCosts.getFormattedCost(cost),
//            ResourceCosts.getFormattedPopAndTime(cost),
//            FormattedCharSequence.forward("", Style.EMPTY),
//            FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.parrot.tooltip1"), Style.EMPTY),
//            FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.parrot.tooltip2"), Style.EMPTY)
//        ));
//
//        return new StartProductionButton(
//            GruntProd.itemName,
//            ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/mobheads/parrot.png"),
//            hotkey,
//            () -> false,
//            () -> true,
//            tooltipLines,
//            this
//        );
//    }
//
//    public StopProductionButton getCancelButton(ProductionPlacement prodBuilding, boolean first) {
//        return new StopProductionButton(
//            ParrotProd.itemName,
//            ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "textures/mobheads/parrot.png"),
//            prodBuilding,
//            this,
//            first
//        );
//    }
//}