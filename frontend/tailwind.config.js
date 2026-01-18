/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'app-dark': '#1f1f1f',
        'app-surface': '#2c2c2c',
        'app-red': '#e91313',
        'app-red-hover': '#ff2e2e',
        'app-text': '#ffffff',
        'app-text-muted': '#bbbbbb',
        'app-input': '#444444',
      },
      backgroundImage: {
        'library-hero': "url('/bib3.jpg')",
      }
    },
  },
  plugins: [],
}
