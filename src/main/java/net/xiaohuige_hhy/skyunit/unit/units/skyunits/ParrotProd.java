public class ParrotProd extends ProductionItem {

    public final static String itemName = "Parrot";
    public final static ResourceCost cost = ResourceCosts.PARROT;

    public GruntProd() {
        super(cost);
        this.onComplete = (Level level, ProductionPlacement placement) -> {
            if (!level.isClientSide())
                placement.produceUnit((ServerLevel) level, SkyUnitEntityRegistrar.PARROT_UNIT.get(), placement.ownerName, true);
        };
    }

    public String getItemName() {
        return ParrotProd.itemName;
    }

    public UnitSpawnButton getPlaceButton() {
        return new UnitSpawnButton(
                itemName,
                ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/mobheads/parrot.png"),
                List.of(
                        FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.parrot"), Style.EMPTY.withBold(true)),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.parrot.tooltip1"), Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.parrot.tooltip2"), Style.EMPTY)
                )
        );
    }

    public StartProductionButton getStartButton(ProductionPlacement prodBuilding, Keybinding hotkey) {
        List<FormattedCharSequence> tooltipLines = new ArrayList<>(List.of(
            FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.parrot"), Style.EMPTY.withBold(true)),
            ResourceCosts.getFormattedCost(cost),
            ResourceCosts.getFormattedPopAndTime(cost),
            FormattedCharSequence.forward("", Style.EMPTY),
            FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.parrot.tooltip1"), Style.EMPTY),
            FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.parrot.tooltip2"), Style.EMPTY)
        ));

        return new StartProductionButton(
            GruntProd.itemName,
            ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/mobheads/parrot.png"),
            hotkey,
            () -> false,
            () -> true,
            tooltipLines,
            this
        );
    }

    public StopProductionButton getCancelButton(ProductionPlacement prodBuilding, boolean first) {
        return new StopProductionButton(
            GruntProd.itemName,
            ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/mobheads/parrot.png"),
            prodBuilding,
            this,
            first
        );
    }
}