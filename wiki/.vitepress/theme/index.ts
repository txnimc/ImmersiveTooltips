import type { Theme } from 'vitepress'
import DefaultTheme from 'vitepress/theme'
import { enhanceAppWithTabs } from 'vitepress-plugin-tabs/client'
import { h } from 'vue'

import "./theme.css"
import video from './HeroVideo.vue'

export default {
    extends: DefaultTheme,
    Layout() {
        return h(DefaultTheme.Layout, null, {
            'home-hero-image': () => h(video)
        })
    },
    enhanceApp({ app }) {
        enhanceAppWithTabs(app)
    }
} satisfies Theme
