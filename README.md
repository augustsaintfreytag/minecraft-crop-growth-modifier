# Crop Growth Modifier

A mod for Minecraft 1.20.1 to allow globally adjusting the rate of crop growth. Can be configured to either decelerate or accelerate the rate at which crops receive random ticks where they have a chance to grow. Can be configured with Cloth Config from the in-game mod menu. Built for Fabric.

## Features

Crop blocks in Minecraft receive random ticks and internally roll on if the crop should grow into its next maturity state. This mod hooks into the `randomTick` method of `CropBlock` and rolls an additional time to determine if the tick should go through or be prevented. When set to a tick chance of `0.5`, only half of all random ticks sent to a crop block would go through and it should grow only half as quickly. Additionally, the mod supports scheduling extra rolls for the crop to allow for more than one chance to mature.

### Slower Growth

To slow down crop growth in your world, configure crop tick chance (`cropTickChance`) to a value below the default `1.0` (unaltered/vanilla growth speed). Setting it to `0.5` will let crops grow at roughly 50% the speed of vanilla, `0.25` at 25% the speed of vanilla, and so on.

### Faster Growth

To speed up crop growth, leave crop tick chance at its default `1.0` and increase the chance for extra rolls (`cropExtraRollChance`). Setting it to `0.5` means that half the times a crop might grow, it gets another chance to grow again, giving you a cumulative of 75%.

Consider configuring a limit on how many extra rolls can happen via max extra rolls (`cropExtraRollMax`). Setting the extra roll chance to `1.0` causes another tick to be scheduled for every single tick that occurs, so a limit is recommended to prevent infinite growth.

## License

This mod was created by Saint for free use by the Minecraft community with basic attribution under the MIT license. This mod may be distributed with mod packs.