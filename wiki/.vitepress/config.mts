import { defineConfig, PageData, TransformPageContext } from 'vitepress'
import { applySEO } from './seo';

const req = await fetch(
    'https://raw.githubusercontent.com/nishtahir/language-kotlin/master/dist/Kotlin.JSON-tmLanguage'
)

const kotlin2 = JSON.parse(
    JSON.stringify(await req.json()).replace(/Kotlin/gi, 'kotlin2')
)

// https://vitepress.dev/reference/site-config
export default defineConfig({
  lang: 'en-US',
  title: "Immersive Messages API",
  description: "Minecraft Library for Communicating with Players",
  cleanUrls: true,
  appearance: 'force-dark',

  head: [[
    'link',
    { rel: 'icon', sizes: '32x32', href: '/assets/logo.png' },
  ]],

  // @ts-ignore
  transformPageData: (pageData: PageData, _ctx: TransformPageContext) => {
    applySEO(pageData);
  },

  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    outline: {
      level: "deep"
    },
    logo: "/assets/logo.png",
    search: {
      provider: 'local'
    },
    nav: [
      { text: 'Home', link: '/' },
      { text: 'Getting Started', link: '/guide' }
    ],

    sidebar: [
      {
        text: 'Template Setup',
        items: [
          { text: 'Getting Started', link: '/guide', items: [
              { text: 'Sending Messages', link: '/api' },
              { text: 'Styling Messages', link: '/styling' },
              { text: 'Animating Messages', link: '/animation' },
              { text: 'Message Presets', link: '/presets' }
            ]
          },
          { text: 'Command Reference', link: '/commands' }
        ]
      }
    ],

    socialLinks: [
      { icon: 'github', link: 'https://github.com/txnimc/TxniTemplate' },
      { icon: 'discord', link: 'https://discord.gg/kS7auUeYmc'}
    ],
    sitemap: {
      hostname: "https://template.txni.dev/"
    },
    markdown: {
      languages: [kotlin2],
      languageAlias: {
        kotlin: 'kotlin2',
        kt: 'kotlin2',
        kts: 'kotlin2'
      }
    }
  }
})
