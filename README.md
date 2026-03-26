# McLookup

A lightweight Kotlin library for looking up Minecraft player information via the official Mojang APIs.

## Features

- Look up a player by **username** or **UUID**
- Retrieve **player name**, **skin URL**, and **cape URL**
- Download the full **skin image** or just the **head portrait**
- Export skins and heads as **Base64 data URLs** (ready for web embedding)
- Scale player heads with **nearest-neighbor interpolation** for pixel-perfect display

## Installation

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://repo.comae.dev/repository/maven-releases/")
}

dependencies {
    implementation("dev.comae:McLookup:1.0.1")
}
```

### Gradle (Groovy)

```groovy
repositories {
    maven { url "https://repo.comae.dev/repository/maven-releases/" }
}

dependencies {
    implementation "dev.comae:McLookup:1.0.1"
}
```

## Usage

### Look up by player name

```kotlin
val account = McAccount("Notch").load()

println(account.playerName)          // "Notch"
println(account.uuid)                // UUID object
println(account.getPlayerSkinURL())  // https://textures.minecraft.net/...
```

### Look up by UUID

```kotlin
val account = McAccount(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5")).load()

println(account.playerName)  // "Notch"
```

### Get skin image

```kotlin
val skinImage: BufferedImage = account.getPlayerSkinImage()
ImageIO.write(skinImage, "png", File("skin.png"))
```

### Get head portrait

```kotlin
// Raw 8x8 head image
val headImage: BufferedImage = account.getPlayerHeadImage()

// Scaled up (e.g. 16x → 128x128), returns a Base64 data URL
val headDataUrl: String = account.getPlayerHeadDataUrl(scale = 16)
// -> "data:image/png;base64,..."
```

### Check for a cape

```kotlin
if (account.playerGotCape()) {
    println(account.getPlayerCapeURL())
}
```

### Get skin as a data URL

```kotlin
val dataUrl: String = account.getPlayerSkinDataUrl()
// -> "data:image/png;base64,..."
```

## API Reference

### `McAccount(playerName: String)`

Resolves the username to a UUID via the Mojang API. Throws `UnknownPlayerException` if the player does not exist.

### `McAccount(uuid: UUID)`

Creates an account object from a known UUID without any network call.

### `fun load(): McAccount`

Fetches the player's profile (name and texture data) from Mojang's session server. Must be called before using any skin/cape methods. Returns `this` for chaining.

```kotlin
val account = McAccount("Notch").load()
```

### Methods

| Method | Return | Description |
|---|---|---|
| `getPlayerSkinURL()` | `String` | Direct URL to the player's skin texture |
| `playerGotCape()` | `Boolean` | Whether the player owns a cape |
| `getPlayerCapeURL()` | `String` | Direct URL to the player's cape (empty string if none) |
| `getPlayerSkinImage()` | `BufferedImage` | The full 64x64 skin image |
| `getPlayerHeadImage()` | `BufferedImage` | The 8x8 head portrait cropped from the skin |
| `getPlayerSkinDataUrl()` | `String` | Skin as a Base64 PNG data URL |
| `getPlayerHeadDataUrl(scale)` | `String` | Head as a scaled Base64 PNG data URL (default scale = 1) |

## Exceptions

- `UnknownPlayerException` — thrown by the `McAccount(String)` constructor when the username is not found.

## License

[Apache License 2.0](LICENSE)
