---
outline: deep
---

# In Game Command API

For people who are not mod developers, Immersive Messages adds a few commands for sending messages.


# /immersivemessages send

This function sends a basic, typewriter message with sound effects above the player's hotbar, 
with a duration and selector. It can send to multiple players at once.

```mclang
/immersivemessages send @a 5 message
```

# /immersivemessages toast

This function is just like the one above, but will send a [Toast preset](/presets). 
The main difference here is that the title must be in quotes.

```mclang
/immersivemessages toast @a 5 "title" subtitle
```

# /immersivemessages popup

Similar to toast, but for popups.

```mclang
/immersivemessages popup @a 5 "title" subtitle
```

# /immersivemessages sendcustom

This function is where you can send fully customized messages using NBT. For example:

```mclang
/immersivemessages sendcustom @a {anchor:4, background:1, wrap:1, typewriter:1, align:0} 8 this is a test message
```

### Supported Parameters
- String
  - `color` - takes hexadecimal color code
  - `bgColor` - takes color code
  - `borderTop` - takes color code
  - `borderBottom` - takes color code
  - `font` - takes ResourceLocation, for example, `immersivemessages:roboto`
  - `anchor` - takes TextAnchor, for example, `CENTER_CENTER` or `BOTTOM_RIGHT`
  - `align` - takes TextAnchor, see above
- Float
  - `x`
  - `y`
  - `size`
- Boolean (can be set to 1)
  - `typewriter`
  - `sound`
  - `bold`
  - `italic`
  - `wrap`
  - `background`
  - `rainbow`
  - `shake`
  - `wave`
  - `obfuscate`
  - `slideup`
  - `slidedown`
  - `slideleft`
  - `slideright`
  - `slideoutup`
  - `slideoutdown`
  - `slideoutleft`
  - `slideoutright`


 