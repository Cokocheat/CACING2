package com.example.data

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val flagEmoji: String
) {
    INDONESIAN("id", "Indonesia", "🇮🇩"),
    ENGLISH("en", "English", "🇺🇸"),
    KOREAN("ko", "한국어", "🇰🇷"),
    TURKISH("tr", "Türkçe", "🇹🇷"),
    ARABIC("ar", "العربية", "🇸🇦"),
    VIETNAMESE("vi", "Tiếng Việt", "🇻🇳"),
    TAIWAN("zh_tw", "繁體中文", "🇹🇼");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code == code } ?: INDONESIAN
        }
    }
}

object StringsHelper {
    fun get(key: String, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.INDONESIAN -> indonesianStrings[key] ?: englishStrings[key] ?: key
            AppLanguage.ENGLISH -> englishStrings[key] ?: key
            AppLanguage.KOREAN -> koreanStrings[key] ?: englishStrings[key] ?: key
            AppLanguage.TURKISH -> turkishStrings[key] ?: englishStrings[key] ?: key
            AppLanguage.ARABIC -> arabicStrings[key] ?: englishStrings[key] ?: key
            AppLanguage.VIETNAMESE -> vietnameseStrings[key] ?: englishStrings[key] ?: key
            AppLanguage.TAIWAN -> taiwanStrings[key] ?: englishStrings[key] ?: key
        }
    }

    private val indonesianStrings = mapOf(
        "app_name" to "nana tayantuh",
        "play" to "MULAI MAIN",
        "outer_arena" to "Arena Luar",
        "time_attack" to "Time Attack",
        "battle_royale" to "Battle Royale",
        "wardrobe" to "Lemari Skin",
        "upgrades" to "Peningkatan",
        "records" to "Statistik",
        "settings" to "Pengaturan",
        "daily_event" to "Event 7 Hari",
        "coins" to "Koin Emas",
        "player_name" to "Nama Cacing Kamu",
        "choose_mode" to "PILIH MODE PERMAINAN:",
        "choose_theme" to "PILIH TEMA ARENA:",
        "high_score" to "Rekor Terbaik",
        "kills" to "Lawan Dikalahkan",
        "points" to "Poin",
        "worms_count" to "Cacing",
        "change_skin" to "Ganti Skin",

        // Themes
        "theme_cyber_funk" to "Cyber Funk",
        "theme_green_meadow" to "Green Meadow",
        "theme_sweet_candy" to "Sweet Candy",
        "theme_cosmic_space" to "Cosmic Space",
        "theme_lava_volcano" to "Lava Volcano",
        "theme_cyber_desc" to "Grid laser neon berteknologi tinggi futuristik",
        "theme_meadow_desc" to "Padang rumput hijau segar nan asri",
        "theme_candy_desc" to "Dunia permen manis gula-gula penuh warna",
        "theme_space_desc" to "Galaksi kosmik antariksa dengan taburan bintang",
        "theme_volcano_desc" to "Kawah magma berapi membara dengan kerak lava",

        // Joystick Settings
        "joystick_settings" to "PENGATURAN JOYSTICK",
        "joystick_mode_static" to "Mode Diam (Posisi Tetap)",
        "joystick_mode_static_desc" to "Joystick diam di posisi sudut yang telah ditentukan",
        "joystick_mode_dynamic" to "Mode Bergerak (Mengikuti Jari)",
        "joystick_mode_dynamic_desc" to "Joystick otomatis muncul di mana pun jempolmu menyentuh layar",
        "edit_layout" to "Ubah Posisi Tombol Langsung",
        "edit_layout_subtitle" to "Sentuh & geser langsung tombol joystick & boost ke posisi favoritmu",
        "save_layout" to "Simpan Tata Letak",
        "reset_layout" to "Reset Posisi Default",
        "cancel" to "Batal",
        "joystick_size" to "Ukuran Joystick",
        "boost_size" to "Ukuran Tombol Turbo",

        // Sound & Vibration
        "sound" to "Efek Suara",
        "sound_desc" to "Suara gameplay, kunyahan makanan & eliminasi",
        "vibration" to "Getaran Taktil Haptic",
        "vibration_desc" to "Getaran saat eliminasi cacing lawan & turbo boost",
        "language" to "Bahasa Permainan",
        "reset_controls" to "Kembalikan Tata Letak Semula",
        "back" to "Kembali",
        "save" to "Simpan",

        // Gameplay HUD
        "tap_to_steer" to "👆 Geser jempol atau sentuh layar untuk mengarahkan",
        "turbo_boost" to "TURBO",
        "score" to "Skor",
        "leaderboard" to "Peringkat",
        "you" to "(Kamu)",
        "kill_notif_prefix" to "+1 Kamu mengalahkan",
        "pause" to "Jeda",
        "resume" to "Lanjutkan",

        // Daily Login
        "daily_title" to "HADIAH LOGIN 7 HARI",
        "claim" to "KLAIM SEKARANG",
        "claimed_today" to "Sudah diklaim hari ini! Tersisa:",
        "claimed" to "Sudah Diklaim",
        "day" to "Hari",
        "close" to "Tutup",
        "daily_banner_info" to "Masuk setiap hari untuk mengumpulkan koin & skin langka!",

        // Game Over / Revive
        "revive_title" to "LANJUTKAN CACING?",
        "revive_desc" to "Cacingmu tumbang! Bangkitkan kembali dengan perisai kebal 6 detik!",
        "revive_btn" to "HIDUPKAN LAGI",
        "give_up" to "Selesai & Lihat Skor",
        "game_over" to "KAMU KALAH!",
        "victory" to "JUARA ARENA!",
        "play_again" to "MAIN LAGI",
        "main_menu" to "MENU UTAMA",

        // Splash Loading
        "loading" to "MEMUAT PERMAINAN...",
        "tip1" to "Menyiapkan Arena Luar nan Megah...",
        "tip2" to "Menata Donat, Burger, dan Semangka HD...",
        "tip3" to "Menyiapkan Bot Cacing AI yang Menantang...",
        "tip4" to "Siap Meluncur ke Arena Luar!",

        // Wardrobe
        "wardrobe_title" to "LEMARI SKIN & AKSESORIS",
        "skins_tab" to "Skin Cacing",
        "accessories_tab" to "Aksesori Wajah",
        "equip" to "PAKAI SEKARANG",
        "equipped" to "SEDANG DIPAKAI",
        "buy" to "BELI",
        "preview" to "Lihat Pratinjau",

        // Upgrades
        "upgrades_title" to "PENINGKATAN KEMAMPUAN",
        "upgrade_btn" to "TINGKATKAN",
        "max_level" to "LEVEL MAKSIMAL",

        // Records
        "records_title" to "STATISTIK & REKOR TERBAIK",
        "total_games" to "Total Pertandingan",
        "total_kills" to "Total Lawan Dikalahkan",
        "max_length" to "Panjang Maksimum"
    )

    private val englishStrings = mapOf(
        "app_name" to "nana tayantuh",
        "play" to "PLAY NOW",
        "outer_arena" to "Outer Arena",
        "time_attack" to "Time Attack",
        "battle_royale" to "Battle Royale",
        "wardrobe" to "Wardrobe",
        "upgrades" to "Upgrades",
        "records" to "Statistics",
        "settings" to "Settings",
        "daily_event" to "7-Day Event",
        "coins" to "Gold Coins",
        "player_name" to "Your Worm Name",
        "choose_mode" to "SELECT GAME MODE:",
        "choose_theme" to "SELECT ARENA THEME:",
        "high_score" to "Best Score",
        "kills" to "Worms Defeated",
        "points" to "Pts",
        "worms_count" to "Worms",
        "change_skin" to "Change Skin",

        // Themes
        "theme_cyber_funk" to "Cyber Funk",
        "theme_green_meadow" to "Green Meadow",
        "theme_sweet_candy" to "Sweet Candy",
        "theme_cosmic_space" to "Cosmic Space",
        "theme_lava_volcano" to "Lava Volcano",
        "theme_cyber_desc" to "Futuristic neon cyber lasers and synthwave grid",
        "theme_meadow_desc" to "Lush green grass and refreshing outdoor world",
        "theme_candy_desc" to "Sweet candy dreamscape full of colorful treats",
        "theme_space_desc" to "Deep cosmic galaxy with stardust and glowing nebulae",
        "theme_volcano_desc" to "Fiery molten magma crust with blazing lava cracks",

        // Joystick Settings
        "joystick_settings" to "JOYSTICK SETTINGS",
        "joystick_mode_static" to "Static Mode (Fixed Position)",
        "joystick_mode_static_desc" to "Joystick stays fixed at your customized corner position",
        "joystick_mode_dynamic" to "Dynamic Mode (Follows Finger)",
        "joystick_mode_dynamic_desc" to "Joystick appears wherever your thumb touches the left screen",
        "edit_layout" to "Direct Button Layout Editor",
        "edit_layout_subtitle" to "Touch & drag joystick & turbo buttons directly to your favorite spot",
        "save_layout" to "Save Layout",
        "reset_layout" to "Reset Defaults",
        "cancel" to "Cancel",
        "joystick_size" to "Joystick Size",
        "boost_size" to "Turbo Button Size",

        // Sound & Vibration
        "sound" to "Sound Effects",
        "sound_desc" to "Gameplay audio, food munching & elimination sounds",
        "vibration" to "Haptic Vibration",
        "vibration_desc" to "Vibration on opponent takedowns & turbo boost",
        "language" to "Game Language",
        "reset_controls" to "Reset Control Layout",
        "back" to "Back",
        "save" to "Save",

        // Gameplay HUD
        "tap_to_steer" to "👆 Drag thumb or touch screen to steer your worm",
        "turbo_boost" to "TURBO",
        "score" to "Score",
        "leaderboard" to "Leaderboard",
        "you" to "(You)",
        "kill_notif_prefix" to "+1 You defeated",
        "pause" to "Pause",
        "resume" to "Resume",

        // Daily Login
        "daily_title" to "7-DAY LOGIN REWARDS",
        "claim" to "CLAIM NOW",
        "claimed_today" to "Already claimed today! Next in:",
        "claimed" to "Claimed",
        "day" to "Day",
        "close" to "Close",
        "daily_banner_info" to "Log in daily to claim gold coins and exclusive skins!",

        // Game Over / Revive
        "revive_title" to "REVIVE WORM?",
        "revive_desc" to "Your worm fell! Revive with a 6-second invulnerability shield!",
        "revive_btn" to "REVIVE NOW",
        "give_up" to "Give Up & View Score",
        "game_over" to "YOU DIED!",
        "victory" to "ARENA CHAMPION!",
        "play_again" to "PLAY AGAIN",
        "main_menu" to "MAIN MENU",

        // Splash Loading
        "loading" to "LOADING GAME...",
        "tip1" to "Preparing the Grand Outer Arena...",
        "tip2" to "Arranging Donuts, Burgers & Fresh Watermelons...",
        "tip3" to "Spawning Challenging AI Worm Rivals...",
        "tip4" to "Ready to Slither into the Arena!",

        // Wardrobe
        "wardrobe_title" to "SKINS & ACCESSORIES WARDROBE",
        "skins_tab" to "Worm Skins",
        "accessories_tab" to "Face Accessories",
        "equip" to "EQUIP NOW",
        "equipped" to "EQUIPPED",
        "buy" to "BUY",
        "preview" to "Preview",

        // Upgrades
        "upgrades_title" to "STAT UPGRADES",
        "upgrade_btn" to "UPGRADE",
        "max_level" to "MAX LEVEL",

        // Records
        "records_title" to "STATS & BEST RECORDS",
        "total_games" to "Total Matches",
        "total_kills" to "Total Worms Defeated",
        "max_length" to "Maximum Length"
    )

    private val koreanStrings = mapOf(
        "app_name" to "nana tayantuh",
        "play" to "게임 시작",
        "outer_arena" to "외부 아레나",
        "time_attack" to "타임 어택",
        "battle_royale" to "배틀로얄",
        "wardrobe" to "스킨 보관함",
        "upgrades" to "능력 강화",
        "records" to "통계 기록",
        "settings" to "설정",
        "daily_event" to "7일 출석 이벤트",
        "coins" to "골드 코인",
        "player_name" to "지렁이 닉네임",
        "choose_mode" to "게임 모드 선택:",
        "choose_theme" to "아레나 테마 선택:",
        "high_score" to "최고 기록",
        "kills" to "처치한 지렁이",
        "points" to "점",
        "worms_count" to "마리",
        "change_skin" to "스킨 변경",

        // Themes
        "theme_cyber_funk" to "Cyber Funk",
        "theme_green_meadow" to "Green Meadow",
        "theme_sweet_candy" to "Sweet Candy",
        "theme_cosmic_space" to "Cosmic Space",
        "theme_lava_volcano" to "Lava Volcano",
        "theme_cyber_desc" to "미래 지향적 네온 사이버 레이저 그리드",
        "theme_meadow_desc" to "상쾌하고 푸른 잔디와 넓은 자연",
        "theme_candy_desc" to "달콤한 사탕과 다채로운 디저트 세상",
        "theme_space_desc" to "성운과 별빛이 가득한 깊은 우주",
        "theme_volcano_desc" to "뜨거운 마그마와 용암이 흐르는 화산",

        // Joystick Settings
        "joystick_settings" to "조이스틱 설정",
        "joystick_mode_static" to "고정 모드 (위치 고정)",
        "joystick_mode_static_desc" to "조이스틱이 지정된 화면 위치에 고정됩니다",
        "joystick_mode_dynamic" to "이동 모드 (손가락 추적)",
        "joystick_mode_dynamic_desc" to "왼쪽 화면을 터치하는 위치에 조이스틱이 나타납니다",
        "edit_layout" to "버튼 위치 직접 편집",
        "edit_layout_subtitle" to "조이스틱과 부스트 버튼을 직접 드래그하여 배치하세요",
        "save_layout" to "레이아웃 저장",
        "reset_layout" to "기본값 초기화",
        "cancel" to "취소",
        "joystick_size" to "조이스틱 크기",
        "boost_size" to "부스트 버튼 크기",

        // Sound & Vibration
        "sound" to "음향 효과",
        "sound_desc" to "게임플레이, 음식 먹기 및 처치 효과음",
        "vibration" to "햅틱 진동",
        "vibration_desc" to "처치 및 부스트 사용 시 진동",
        "language" to "게임 언어",
        "reset_controls" to "컨트롤 초기화",
        "back" to "뒤로가기",
        "save" to "저장",

        // Gameplay HUD
        "tap_to_steer" to "👆 엄지손가락으로 드래그하여 조종하세요",
        "turbo_boost" to "부스트",
        "score" to "점수",
        "leaderboard" to "순위표",
        "you" to "(나)",
        "kill_notif_prefix" to "+1 처치 완료:",
        "pause" to "일시정지",
        "resume" to "계속하기",

        // Daily Login
        "daily_title" to "7일 출석 보상",
        "claim" to "지금 수령",
        "claimed_today" to "오늘 이미 수령했습니다! 다음 보상까지:",
        "claimed" to "수령 완료",
        "day" to "일차",
        "close" to "닫기",
        "daily_banner_info" to "매일 출석하여 코인과 특별 스킨을 받으세요!",

        // Game Over / Revive
        "revive_title" to "부활하시겠습니까?",
        "revive_desc" to "6초 무적 보호막과 함께 즉시 부활합니다!",
        "revive_btn" to "지금 부활하기",
        "give_up" to "포기하고 결과 보기",
        "game_over" to "사망하셨습니다!",
        "victory" to "아레나 챔피언!",
        "play_again" to "다시 하기",
        "main_menu" to "메인 메뉴",

        // Splash Loading
        "loading" to "게임 로딩 중...",
        "tip1" to "광활한 외부 아레나 준비 중...",
        "tip2" to "맛있는 도넛, 버거 및 수박 배치 중...",
        "tip3" to "도전적인 AI 지렁이 상대 생성 중...",
        "tip4" to "아레나로 진입할 준비 완료!",

        // Wardrobe
        "wardrobe_title" to "스킨 및 액세서리 보관함",
        "skins_tab" to "지렁이 스킨",
        "accessories_tab" to "얼굴 액세서리",
        "equip" to "장착하기",
        "equipped" to "장착 중",
        "buy" to "구매",
        "preview" to "미리보기",

        // Upgrades
        "upgrades_title" to "능력치 업그레이드",
        "upgrade_btn" to "강화하기",
        "max_level" to "최대 레벨",

        // Records
        "records_title" to "통계 및 최고 기록",
        "total_games" to "총 플레이 판수",
        "total_kills" to "총 처치 수",
        "max_length" to "최대 도달 길이"
    )

    private val turkishStrings = mapOf(
        "app_name" to "nana tayantuh",
        "play" to "HEMEN OYNA",
        "outer_arena" to "Dış Arena",
        "time_attack" to "Zaman Saldırısı",
        "battle_royale" to "Battle Royale",
        "wardrobe" to "Görünüm Dolabı",
        "upgrades" to "Yükseltmeler",
        "records" to "İstatistikler",
        "settings" to "Ayarlar",
        "daily_event" to "7 Günlük Etkinlik",
        "coins" to "Altın Sikke",
        "player_name" to "Solucanının Adı",
        "choose_mode" to "OYUN MODU SEÇ:",
        "choose_theme" to "ARENA TEMASI SEÇ:",
        "high_score" to "En Yüksek Skor",
        "kills" to "Yenilen Solucanlar",
        "points" to "Puan",
        "worms_count" to "Solucan",
        "change_skin" to "Görünüm Değiştir",

        // Themes
        "theme_cyber_funk" to "Cyber Funk",
        "theme_green_meadow" to "Green Meadow",
        "theme_sweet_candy" to "Sweet Candy",
        "theme_cosmic_space" to "Cosmic Space",
        "theme_lava_volcano" to "Lava Volcano",
        "theme_cyber_desc" to "Fütüristik neon siber lazer ızgarası",
        "theme_meadow_desc" to "Ferah yeşil çimler ve açık doğa",
        "theme_candy_desc" to "Renkli tatlılar ve şekerleme rüyası",
        "theme_space_desc" to "Yıldız tozlu derin kozmik galaksi",
        "theme_volcano_desc" to "Ateşli lav çatlakları ve akkor magma",

        // Joystick Settings
        "joystick_settings" to "JOYSTİK AYARLARI",
        "joystick_mode_static" to "Sabit Mod (Sabit Konum)",
        "joystick_mode_static_desc" to "Joystik ayarlanan sol alt köşede sabit durur",
        "joystick_mode_dynamic" to "Dinamik Mod (Parmağı Takip Eder)",
        "joystick_mode_dynamic_desc" to "Joystik sol ekranda dokunduğunuz yerde belirir",
        "edit_layout" to "Doğrudan Düğme Düzenleyici",
        "edit_layout_subtitle" to "Joystik ve turbo butonunu istediğiniz yere sürükleyin",
        "save_layout" to "Düzeni Kaydet",
        "reset_layout" to "Varsayılanlara Sıfırla",
        "cancel" to "İptal",
        "joystick_size" to "Joystik Boyutu",
        "boost_size" to "Turbo Düğme Boyutu",

        // Sound & Vibration
        "sound" to "Ses Efektleri",
        "sound_desc" to "Oyun, yeme ve solucan avlama sesleri",
        "vibration" to "Dokunsal Titreşim",
        "vibration_desc" to "Rakip avlama ve turbo sırasında titreşim",
        "language" to "Oyun Dili",
        "reset_controls" to "Kontrolleri Sıfırla",
        "back" to "Geri",
        "save" to "Kaydet",

        // Gameplay HUD
        "tap_to_steer" to "👆 Solucanı yönlendirmek için başparmağınızla sürükleyin",
        "turbo_boost" to "TURBO",
        "score" to "Skor",
        "leaderboard" to "Liderler",
        "you" to "(Sen)",
        "kill_notif_prefix" to "+1 Solucan yendin:",
        "pause" to "Duraklat",
        "resume" to "Devam Et",

        // Daily Login
        "daily_title" to "7 GÜNLÜK GİRİŞ ÖDÜLLERİ",
        "claim" to "ŞİMDİ AL",
        "claimed_today" to "Bugün zaten alındı! Sonraki ödül:",
        "claimed" to "Alındı",
        "day" to "Gün",
        "close" to "Kapat",
        "daily_banner_info" to "Altın ve özel görünümler toplamak için her gün gir!",

        // Game Over / Revive
        "revive_title" to "SOLUCANI CANLANDIR?",
        "revive_desc" to "Solucanın düştü! 6 saniye dokunulmazlık kalkanı ile canlan!",
        "revive_btn" to "CANLANDIR",
        "give_up" to "Pes Et ve Skoru Gör",
        "game_over" to "ÖLDÜN!",
        "victory" to "ARENA ŞAMPİYONU!",
        "play_again" to "TEKRAR OYNA",
        "main_menu" to "ANA MENÜ",

        // Splash Loading
        "loading" to "OYUN YÜKLENİYOR...",
        "tip1" to "Görkemli Dış Arena hazırlanıyor...",
        "tip2" to "Donut, burger ve karpuzlar diziliyor...",
        "tip3" to "Akıllı yapay zeka rakipler oluşturuluyor...",
        "tip4" to "Arenaya kaymaya hazırız!",

        // Wardrobe
        "wardrobe_title" to "GÖRÜNÜM VE AKSESUAR DOLABI",
        "skins_tab" to "Solucan Görünümleri",
        "accessories_tab" to "Yüz Aksesuarları",
        "equip" to "ŞİMDİ KUŞAN",
        "equipped" to "KUŞANILDI",
        "buy" to "SATIN AL",
        "preview" to "Önizleme",

        // Upgrades
        "upgrades_title" to "YETENEK YÜKSELTMELERİ",
        "upgrade_btn" to "YÜKSELT",
        "max_level" to "MAKS SEVİYE",

        // Records
        "records_title" to "İSTATİSTİKLER VE REKORLAR",
        "total_games" to "Toplam Oyun Sayısı",
        "total_kills" to "Toplam Avlanan Solucan",
        "max_length" to "Ulaşılan En Uzun Boy"
    )

    private val arabicStrings = mapOf(
        "app_name" to "nana tayantuh",
        "play" to "العب الآن",
        "outer_arena" to "الساحة الخارجية",
        "time_attack" to "هجوم الوقت",
        "battle_royale" to "باتل رويال",
        "wardrobe" to "خزانة المظهر",
        "upgrades" to "الترقيات",
        "records" to "الإحصائيات",
        "settings" to "الإعدادات",
        "daily_event" to "حدث 7 أيام",
        "coins" to "عملات ذهبية",
        "player_name" to "اسم دودتك",
        "choose_mode" to "اختر وضع اللعبة:",
        "choose_theme" to "اختر سمة الساحة:",
        "high_score" to "أفضل نتيجة",
        "kills" to "الديدان المهزومة",
        "points" to "نقطة",
        "worms_count" to "دودة",
        "change_skin" to "تغيير المظهر",

        // Themes
        "theme_cyber_funk" to "Cyber Funk",
        "theme_green_meadow" to "Green Meadow",
        "theme_sweet_candy" to "Sweet Candy",
        "theme_cosmic_space" to "Cosmic Space",
        "theme_lava_volcano" to "Lava Volcano",
        "theme_cyber_desc" to "شبكة ليزر نيون مستقبلية عالية التقنية",
        "theme_meadow_desc" to "مروج خضراء منعشة وطبيعة واسعة",
        "theme_candy_desc" to "عالم حلويات سكرية ملونة ولذيذة",
        "theme_space_desc" to "مجرة كونية عميقة مليئة بسديم النجوم",
        "theme_volcano_desc" to "فوهة بركانية ملتهبة مع حمم الحمم البركانية",

        // Joystick Settings
        "joystick_settings" to "إعدادات عصا التحكم",
        "joystick_mode_static" to "الوضع الثابت (موضع ثابت)",
        "joystick_mode_static_desc" to "تبقى عصا التحكم ثابتة في موضعها المحدد",
        "joystick_mode_dynamic" to "الوضع المتحرك (يتبع الإصبع)",
        "joystick_mode_dynamic_desc" to "تظهر عصا التحكم حيث يلمس إبهامك الشاشة اليسرى",
        "edit_layout" to "تعديل موضع الأزرار مباشرة",
        "edit_layout_subtitle" to "المس واسحب أزرار التحكم والتسريع مباشرة لمكانك المفضل",
        "save_layout" to "حفظ التخطيط",
        "reset_layout" to "إعادة التعيين الافتراضية",
        "cancel" to "إلغاء",
        "joystick_size" to "حجم عصا التحكم",
        "boost_size" to "حجم زر التسريع",

        // Sound & Vibration
        "sound" to "المؤثرات الصوتية",
        "sound_desc" to "أصوات اللعب وأكل الطعام والانتصار",
        "vibration" to "الاهتزاز اللمسي",
        "vibration_desc" to "الاهتزاز عند القضاء على الديدان والتسريع",
        "language" to "لغة اللعبة",
        "reset_controls" to "إعادة ضبط أزرار التحكم",
        "back" to "رجوع",
        "save" to "حفظ",

        // Gameplay HUD
        "tap_to_steer" to "👆 اسحب بإبهامك أو المس الشاشة لتوجيه الدودة",
        "turbo_boost" to "تسريع",
        "score" to "النتيجة",
        "leaderboard" to "المتصدرين",
        "you" to "(أنت)",
        "kill_notif_prefix" to "+1 لقد هزمت دودة",
        "pause" to "إيقاف",
        "resume" to "استئناف",

        // Daily Login
        "daily_title" to "مكافآت تسجيل الدخول لـ 7 أيام",
        "claim" to "المطالبة الآن",
        "claimed_today" to "تم الاستلام اليوم! المكافأة التالية بعد:",
        "claimed" to "تم الاستلام",
        "day" to "يوم",
        "close" to "إغلاق",
        "daily_banner_info" to "سجل الدخول يومياً لكسب الذهب والمظاهر النادرة!",

        // Game Over / Revive
        "revive_title" to "إحياء الدودة؟",
        "revive_desc" to "سقطت دودتك! قم بإحيائها مع درع مناعة لمدة 6 ثوانٍ!",
        "revive_btn" to "إحياء الآن",
        "give_up" to "استسلام وعرض النتيجة",
        "game_over" to "لقد خسرت!",
        "victory" to "بطل الساحة!",
        "play_again" to "العب مجدداً",
        "main_menu" to "القائمة الرئيسية",

        // Splash Loading
        "loading" to "جارٍ تحميل اللعبة...",
        "tip1" to "تجهيز الساحة الخارجية العظيمة...",
        "tip2" to "ترتيب الدونات والبرغر والبطيخ اللذيذ...",
        "tip3" to "تجهيز منافسي الديدان الأذكياء...",
        "tip4" to "مستعد للانزلاق نحو الساحة!",

        // Wardrobe
        "wardrobe_title" to "خزانة المظاهر والإكسسوارات",
        "skins_tab" to "مظاهر الدودة",
        "accessories_tab" to "إكسسوارات الوجه",
        "equip" to "تجهيز الآن",
        "equipped" to "مُجهّز",
        "buy" to "شراء",
        "preview" to "معاينة",

        // Upgrades
        "upgrades_title" to "ترقية القدرات",
        "upgrade_btn" to "ترقية",
        "max_level" to "المستوى الأقصى",

        // Records
        "records_title" to "الإحصائيات وأفضل السجلات",
        "total_games" to "إجمالي المباريات",
        "total_kills" to "إجمالي الخصوم المهزومين",
        "max_length" to "أقصى طول محقق"
    )

    private val vietnameseStrings = mapOf(
        "app_name" to "nana tayantuh",
        "play" to "BẮT ĐẦU CHƠI",
        "outer_arena" to "Đấu Trường Ngoài",
        "time_attack" to "Chạy Đua Thời Gian",
        "battle_royale" to "Sinh Tồn Battle Royale",
        "wardrobe" to "Tủ Trang Phục",
        "upgrades" to "Nâng Cấp",
        "records" to "Thống Kê",
        "settings" to "Cài Đặt",
        "daily_event" to "Sự Kiện 7 Ngày",
        "coins" to "Xu Vàng",
        "player_name" to "Tên Giun Của Bạn",
        "choose_mode" to "CHỌN CHẾ ĐỘ CHƠI:",
        "choose_theme" to "CHỌN CHỦ ĐỀ ĐẤU TRƯỜNG:",
        "high_score" to "Điểm Cao Nhất",
        "kills" to "Đối Thủ Bị Hạ",
        "points" to "Điểm",
        "worms_count" to "Con Giun",
        "change_skin" to "Đổi Trang Phục",

        // Themes
        "theme_cyber_funk" to "Cyber Funk",
        "theme_green_meadow" to "Green Meadow",
        "theme_sweet_candy" to "Sweet Candy",
        "theme_cosmic_space" to "Cosmic Space",
        "theme_lava_volcano" to "Lava Volcano",
        "theme_cyber_desc" to "Lưới laser neon công nghệ cao phong cách tương lai",
        "theme_meadow_desc" to "Thảo nguyên cỏ xanh mát mẻ ngập tràn thiên nhiên",
        "theme_candy_desc" to "Xứ sở kẹo ngọt sắc màu rực rỡ và đồ ngọt hấp dẫn",
        "theme_space_desc" to "Thiên hà vũ trụ sâu thẳm lấp lánh tinh vân",
        "theme_volcano_desc" to "Miệng núi lửa rực lửa với các vết nứt dung nham nóng bỏng",

        // Joystick Settings
        "joystick_settings" to "CÀI ĐẶT CẦN ĐIỀU KHIỂN",
        "joystick_mode_static" to "Chế Độ Tĩnh (Vị Trí Cố Định)",
        "joystick_mode_static_desc" to "Cần điều khiển ở vị trí cố định góc dưới bên trái",
        "joystick_mode_dynamic" to "Chế Độ Động (Theo Ngón Tay)",
        "joystick_mode_dynamic_desc" to "Cần điều khiển tự xuất hiện nơi ngón tay chạm màn hình trái",
        "edit_layout" to "Chỉnh Vị Trí Nút Trực Tiếp",
        "edit_layout_subtitle" to "Chạm & kéo trực tiếp nút cần gạt và tăng tốc tới vị trí ưa thích",
        "save_layout" to "Lưu Bố Cục",
        "reset_layout" to "Đặt Lại Mặc Định",
        "cancel" to "Hủy",
        "joystick_size" to "Kích Thước Cần Gạt",
        "boost_size" to "Kích Thước Nút Turbo",

        // Sound & Vibration
        "sound" to "Hiệu Ứng Âm Thanh",
        "sound_desc" to "Âm thanh chơi game, ăn mồi & hạ gục đối thủ",
        "vibration" to "Rung Phản Hồi",
        "vibration_desc" to "Rung khi tiêu diệt sâu đối thủ và tăng tốc turbo",
        "language" to "Ngôn Ngữ Trò Chơi",
        "reset_controls" to "Đặt Lại Phím Điều Khiển",
        "back" to "Quay Lại",
        "save" to "Lưu",

        // Gameplay HUD
        "tap_to_steer" to "👆 Trượt ngón tay cái để điều khiển hướng giun",
        "turbo_boost" to "TĂNG TỐC",
        "score" to "Điểm",
        "leaderboard" to "Bảng Xếp Hạng",
        "you" to "(Bạn)",
        "kill_notif_prefix" to "+1 Bạn đã hạ gục",
        "pause" to "Tạm Dừng",
        "resume" to "Tiếp Tục",

        // Daily Login
        "daily_title" to "PHẦN THƯỞNG 7 NGÀY ĐĂNG NHẬP",
        "claim" to "NHẬN NGAY",
        "claimed_today" to "Hôm nay đã nhận! Phần thưởng kế tiếp sau:",
        "claimed" to "Đã Nhận",
        "day" to "Ngày",
        "close" to "Đóng",
        "daily_banner_info" to "Đăng nhập mỗi ngày để tích lũy xu và nhận skin hiếm!",

        // Game Over / Revive
        "revive_title" to "HỒI SINH GIUN?",
        "revive_desc" to "Giun của bạn đã gục ngã! Hồi sinh ngay với khiên bất tử 6 giây!",
        "revive_btn" to "HỒI SINH NGAY",
        "give_up" to "Xem Điểm & Kết Thúc",
        "game_over" to "BẠN ĐÃ THUA!",
        "victory" to "QUÁN QUÂN ĐẤU TRƯỜNG!",
        "play_again" to "CHƠI LẠI",
        "main_menu" to "MENU CHÍNH",

        // Splash Loading
        "loading" to "ĐANG TẢI TRÒ CHƠI...",
        "tip1" to "Đang chuẩn bị Đấu trường Ngoài rộng lớn...",
        "tip2" to "Bày biện bánh donut, burger và dưa hấu HD...",
        "tip3" to "Triệu hồi các đối thủ giun AI thông minh...",
        "tip4" to "Sẵn sàng trườn vào đấu trường!",

        // Wardrobe
        "wardrobe_title" to "TỦ TRANG PHỤC & PHỤ KIỆN",
        "skins_tab" to "Skin Giun",
        "accessories_tab" to "Phụ Kiện Mặt",
        "equip" to "TRANG BỊ NGAY",
        "equipped" to "ĐANG TRANG BỊ",
        "buy" to "MUA",
        "preview" to "Xem Thử",

        // Upgrades
        "upgrades_title" to "NÂNG CẤP KỸ NĂNG",
        "upgrade_btn" to "NÂNG CẤP",
        "max_level" to "CẤP TỐI ĐA",

        // Records
        "records_title" to "THỐNG KÊ & KỶ LỤC TỐT NHẤT",
        "total_games" to "Tổng Số Trận",
        "total_kills" to "Tổng Đối Thủ Đã Hạ",
        "max_length" to "Chiều Dài Kỷ Lục"
    )

    private val taiwanStrings = mapOf(
        "app_name" to "nana tayantuh",
        "play" to "開始遊戲",
        "outer_arena" to "外圈競技場",
        "time_attack" to "計時挑戰賽",
        "battle_royale" to "大逃殺模式",
        "wardrobe" to "皮膚衣櫃",
        "upgrades" to "能力強化",
        "records" to "生涯統計",
        "settings" to "遊戲設置",
        "daily_event" to "7日簽到活動",
        "coins" to "金幣",
        "player_name" to "你的蟲蟲暱稱",
        "choose_mode" to "選擇遊戲模式:",
        "choose_theme" to "選擇競技場主題:",
        "high_score" to "歷史最高分",
        "kills" to "擊敗蟲蟲總數",
        "points" to "分",
        "worms_count" to "隻",
        "change_skin" to "更換皮膚",

        // Themes
        "theme_cyber_funk" to "Cyber Funk",
        "theme_green_meadow" to "Green Meadow",
        "theme_sweet_candy" to "Sweet Candy",
        "theme_cosmic_space" to "Cosmic Space",
        "theme_lava_volcano" to "Lava Volcano",
        "theme_cyber_desc" to "未來科技感賽博霓虹激光網格",
        "theme_meadow_desc" to "清新翠綠的戶外大草原世界",
        "theme_candy_desc" to "繽紛夢幻的糖果甜點世界",
        "theme_space_desc" to "星雲星塵環繞的深邃浩瀚宇宙",
        "theme_volcano_desc" to "灼熱熔岩岩漿滾滾的烈火火山",

        // Joystick Settings
        "joystick_settings" to "搖桿設置",
        "joystick_mode_static" to "固定模式 (固定位置)",
        "joystick_mode_static_desc" to "搖桿保持在自定義的左下角固定位置",
        "joystick_mode_dynamic" to "浮動模式 (跟隨手指)",
        "joystick_mode_dynamic_desc" to "手指觸碰左側屏幕任意位置即刻生成搖桿",
        "edit_layout" to "直接自定義按鍵佈局",
        "edit_layout_subtitle" to "觸碰並拖動搖桿與加速鍵至喜愛的位置",
        "save_layout" to "保存佈局",
        "reset_layout" to "恢復默認",
        "cancel" to "取消",
        "joystick_size" to "搖桿大小",
        "boost_size" to "極速衝刺鍵大小",

        // Sound & Vibration
        "sound" to "遊戲音效",
        "sound_desc" to "遊戲操作、吞噬美食與擊殺音效",
        "vibration" to "觸覺振動反饋",
        "vibration_desc" to "擊殺對手與極速衝刺時的振動反饋",
        "language" to "遊戲語言",
        "reset_controls" to "重置按鍵設置",
        "back" to "返回",
        "save" to "保存",

        // Gameplay HUD
        "tap_to_steer" to "👆 滑動手指或點擊屏幕操控蟲蟲轉向",
        "turbo_boost" to "極速衝刺",
        "score" to "得分",
        "leaderboard" to "排行榜",
        "you" to "(你)",
        "kill_notif_prefix" to "+1 擊殺對手:",
        "pause" to "暫停",
        "resume" to "繼續遊戲",

        // Daily Login
        "daily_title" to "7日連續登錄獎勵",
        "claim" to "立即領取",
        "claimed_today" to "今日已領取！距離下次獎勵:",
        "claimed" to "已領取",
        "day" to "第",
        "close" to "關閉",
        "daily_banner_info" to "每日連續登錄領取豐厚金幣與專屬絕版皮膚！",

        // Game Over / Revive
        "revive_title" to "是否復活蟲蟲？",
        "revive_desc" to "你的蟲蟲倒下了！立刻帶著6秒無敵護盾滿血復活！",
        "revive_btn" to "立即復活",
        "give_up" to "結算成績",
        "game_over" to "遊戲結束！",
        "victory" to "競技場霸主！",
        "play_again" to "再來一局",
        "main_menu" to "返回主選單",

        // Splash Loading
        "loading" to "遊戲載入中...",
        "tip1" to "正在準備宏偉寬廣的外圈競技場...",
        "tip2" to "正在佈置美味可口的甜甜圈與漢堡...",
        "tip3" to "正在召集富有挑戰性的AI對手蟲蟲...",
        "tip4" to "準備就緒，衝入競技場大顯身手！",

        // Wardrobe
        "wardrobe_title" to "皮膚與配飾衣櫃",
        "skins_tab" to "蟲蟲皮膚",
        "accessories_tab" to "面部配飾",
        "equip" to "立即裝備",
        "equipped" to "使用中",
        "buy" to "購買",
        "preview" to "預覽效果",

        // Upgrades
        "upgrades_title" to "能力屬性升級",
        "upgrade_btn" to "升級",
        "max_level" to "已達最高級",

        // Records
        "records_title" to "統計成就與最佳紀錄",
        "total_games" to "總對局場次",
        "total_kills" to "擊殺蟲蟲總數",
        "max_length" to "歷史最長身長"
    )
}
