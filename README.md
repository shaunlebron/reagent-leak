Minimal reproducible example for a memory leak, _[reagent issue #637](https://github.com/reagent-project/reagent/issues/637)_.

1. `npm ci`
2. `npx shadow-cljs watch foo`
3. Open in Chrome: <http://localhost:9000>
4. Observe Devtools > Memory tab > JS Heap Size
    * WARNING: JS Heap will quickly reach 2gb and climb until crashing

Discussion:
- <https://clojurians.slack.com/archives/C0620C0C8/p1755961982494269>
