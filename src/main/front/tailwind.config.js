/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}"],
  theme: {
    extend: {
      colors: {
        customHover: '#202123', // 사용자 정의 색상 추가
        customHoverText: '#F9F9F9', // 사용자 정의 글자색
      },
    },
  },
  plugins: [],
}

