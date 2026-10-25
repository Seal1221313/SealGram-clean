package org.telegram.messenger;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Localized strings for SealGram-specific screens and helper dialogs.
 * Index order: English, Russian, Ukrainian, Kazakh.
 */
public final class SealGramI18n {
    private static final Map<String, String[]> STRINGS = new HashMap<>();

    static {
        add("HubVersionPrefix", "Version ", "Версия ", "Версія ", "Нұсқа ");
        add("HubHeaderCategories", "Categories", "Разделы", "Категорії", "Бөлімдер");
        add("HubGeneral", "General", "Общие", "Загальні", "Жалпы");
        add("HubAppearance", "Appearance", "Внешний вид", "Вигляд", "Көрініс");
        add("HubChats", "Chats", "Чаты", "Чати", "Чаттар");
        add("HubVoiceToText", "Voice to text", "Голос в текст", "Голос у текст", "Дауысты мәтінге айналдыру");
        add("HubAutoReply", "Auto-reply", "Автоответчик", "Автовідповідач", "Автоматты жауап");
        add("GhostDelay", "Delayed sending", "Отложенная отправка", "Відкладене надсилання", "Кешіктіріп жіберу");
        add("GhostDelayDesc", "In ghost mode, messages are scheduled about 12 seconds ahead, so you can leave before they are delivered. They sit in the scheduled list until sent.", "В режиме призрака сообщения планируются примерно на 12 секунд вперёд, чтобы ты успел выйти до доставки. До отправки они лежат в списке запланированных.", "У режимі привида повідомлення плануються приблизно на 12 секунд уперед, щоб ти встиг вийти до доставки. До надсилання вони лежать у списку запланованих.", "Елес режимінде хабарламалар шамамен 12 секундқа жоспарланады, жеткізілгенге дейін шығып үлгересіз. Жіберілгенше олар жоспарланғандар тізімінде тұрады.");
        add("StyleTitle", "Fonts and themes", "Шрифт и темы", "Шрифт і теми", "Қаріп және тақырыптар");
        add("FontHeader", "Font", "Шрифт", "Шрифт", "Қаріп");
        add("FontUse", "Custom font", "Свой шрифт", "Власний шрифт", "Өз қарпім");
        add("FontUseDesc", "Applies your font file to the whole app after a restart.", "Применяет твой файл шрифта ко всему приложению после перезапуска.", "Застосовує твій файл шрифту до всього застосунку після перезапуску.", "Қаріп файлыңызды қайта іске қосқаннан кейін бүкіл қолданбаға қолданады.");
        add("FontPick", "Choose font file (.ttf / .otf)", "Выбрать файл шрифта (.ttf / .otf)", "Обрати файл шрифту (.ttf / .otf)", "Қаріп файлын таңдау (.ttf / .otf)");
        add("FontReset", "Reset font", "Сбросить шрифт", "Скинути шрифт", "Қарпті қалпына келтіру");
        add("FontApplied", "Saved. Restart the app to apply the font.", "Сохранено. Перезапусти приложение, чтобы применить шрифт.", "Збережено. Перезапусти застосунок, щоб застосувати шрифт.", "Сақталды. Қарыпті қолдану үшін қолданбаны қайта іске қосыңыз.");
        add("FontInvalid", "Could not read this font file.", "Не удалось прочитать этот файл шрифта.", "Не вдалося прочитати цей файл шрифту.", "Бұл қаріп файлын оқу мүмкін болмады.");
        add("ThemeHeader", "Create your own theme", "Создай свою тему", "Створи свою тему", "Өз тақырыбыңызды жасаңыз");
        add("ThemeAccent", "Accent color", "Акцентный цвет", "Акцентний колір", "Акцент түсі");
        add("ThemeBackground", "Background color", "Цвет фона", "Колір фону", "Фон түсі");
        add("ThemeBubble", "Outgoing bubble color", "Цвет исходящих сообщений", "Колір вихідних повідомлень", "Шығыс хабарлама түсі");
        add("ThemeApply", "Create and apply theme", "Создать и применить тему", "Створити та застосувати тему", "Тақырыпты жасап қолдану");
        add("ThemeApplied", "Theme applied", "Тема применена", "Тему застосовано", "Тақырып қолданылды");
        add("ThemeFailed", "Could not create the theme", "Не удалось создать тему", "Не вдалося створити тему", "Тақырыпты жасау мүмкін болмады");
        add("ThemeHex", "Color as #RRGGBB", "Цвет в формате #RRGGBB", "Колір у форматі #RRGGBB", "Түс #RRGGBB пішімінде");
        add("ThemeInvalidColor", "Invalid color", "Неверный цвет", "Невірний колір", "Қате түс");
        add("ThemeNote", "Pick three colors and tap Create. The theme is saved with your other themes in Telegram’s theme list.", "Выбери три цвета и нажми «Создать». Тема сохранится среди других тем в списке тем.", "Обери три кольори й натисни «Створити». Тема збережеться серед інших тем у списку тем.", "Үш түсті таңдап, «Жасау» түймесін басыңыз. Тақырып басқа тақырыптармен бірге сақталады.");
        add("HubPerf", "Performance mode", "Режим производительности", "Режим продуктивності", "Өнімділік режимі");
        add("HubPerfDesc", "Turns off heavy animations and effects for a faster, smoother app on weaker devices.", "Отключает тяжёлые анимации и эффекты, чтобы приложение работало быстрее и плавнее на слабых устройствах.", "Вимикає важкі анімації та ефекти, щоб застосунок працював швидше й плавніше на слабких пристроях.", "Әлсіз құрылғыларда қолданба жылдамырақ әрі тегіс жұмыс істеуі үшін ауыр анимациялар мен эффектілерді өшіреді.");
        add("KeepDeleted", "Keep deleted messages", "Сохранять удалённые сообщения", "Зберігати видалені повідомлення", "Жойылған хабарларды сақтау");
        add("KeepDeletedDesc", "When someone deletes a message for everyone, its text, photo or voice message stays on this device with a 🗑 mark. Files, self-destructing media and secret chats are never kept. Stored only locally; off by default.", "Когда кто-то удаляет сообщение у всех, его текст, фото или голосовое остаются на этом устройстве с отметкой 🗑. Файлы, самоуничтожающиеся медиа и секретные чаты не сохраняются. Хранится только локально; по умолчанию выключено.", "Коли хтось видаляє повідомлення в усіх, його текст, фото чи голосове лишаються на цьому пристрої з позначкою 🗑. Файли, медіа, що самознищуються, і секретні чати не зберігаються. Зберігається лише локально; за замовчуванням вимкнено.", "Біреу хабарламаны бәріне жойғанда, оның мәтіні, фотосы немесе дауыстық хабары осы құрылғыда 🗑 белгісімен қалады. Файлдар, өздігінен жойылатын медиа және құпия чаттар сақталмайды. Тек жергілікті сақталады; әдепкіде өшірулі.");
        add("KeepWhat", "What to keep", "Что сохранять", "Що зберігати", "Нені сақтау");
        add("KeepText", "Text", "Текст", "Текст", "Мәтін");
        add("KeepPhotos", "Photos", "Фото", "Фото", "Фотолар");
        add("KeepVoice", "Voice messages", "Голосовые", "Голосові", "Дауыстық хабарлар");
        add("KeepStorage", "Storage", "Хранение", "Зберігання", "Сақтау");
        add("KeepDays", "Keep for", "Хранить", "Зберігати", "Сақтау мерзімі");
        add("KeepDaysN", "%1$d d", "%1$d дн.", "%1$d дн.", "%1$d күн");
        add("KeepClear", "Delete all saved now", "Удалить все сохранённые сейчас", "Видалити всі збережені зараз", "Барлық сақталғанды қазір жою");
        add("KeepClearConfirm", "Delete all saved deleted messages from this device?", "Удалить все сохранённые удалённые сообщения с этого устройства?", "Видалити всі збережені видалені повідомлення з цього пристрою?", "Осы құрылғыдан сақталған барлық жойылған хабарларды жою керек пе?");
        add("KeepBgHeader", "Background", "Фон", "Фон", "Фон");
        add("KeepBg", "Run in background", "Работать в фоне", "Працювати у фоні", "Фонда жұмыс істеу");
        add("KeepBgDesc", "Keeps a small service running so deletions are saved even when the app is closed. This is Telegram’s Keep-Alive service and shows a notification. Turn it off to save battery. On Xiaomi/Redmi also allow autostart and remove battery limits.", "Держит маленькую службу, чтобы удалённые сообщения сохранялись и при закрытом приложении. Это служба Keep-Alive Telegram, она показывает уведомление. Выключи, чтобы экономить батарею. На Xiaomi/Redmi ещё разреши автозапуск и сними ограничения батареи.", "Тримає невелику службу, щоб видалені повідомлення зберігалися й при закритому застосунку. Це служба Keep-Alive Telegram, вона показує сповіщення. Вимкни, щоб економити батарею. На Xiaomi/Redmi ще дозволь автозапуск і зніми обмеження батареї.", "Қолданба жабық болғанда да жойылған хабарлар сақталуы үшін шағын қызметті жұмыс істетеді. Бұл Telegram-ның Keep-Alive қызметі, хабарландыру көрсетеді. Батареяны үнемдеу үшін өшіріңіз. Xiaomi/Redmi-де автоқосылуға рұқсат беріп, батарея шектеулерін алыңыз.");
        add("KeepLook", "Appearance", "Оформление", "Оформлення", "Көрініс");
        add("KeepTrash", "Trash icon", "Значок корзины", "Значок кошика", "Себет белгішесі");
        add("KeepTrashDesc", "Shows a trash icon next to the time of saved deleted messages.", "Показывает значок корзины рядом со временем у сохранённых удалённых сообщений.", "Показує значок кошика поруч із часом у збережених видалених повідомленнях.", "Сақталған жойылған хабарлардың уақытының жанында себет белгішесін көрсетеді.");
        add("KeepTrashColor", "Trash icon color", "Цвет корзины", "Колір кошика", "Себет түсі");
        add("KeepMsgColor", "Deleted message text color", "Цвет текста удалённых сообщений", "Колір тексту видалених повідомлень", "Жойылған хабар мәтінінің түсі");
        add("KeepColorDefault", "Default", "По умолчанию", "За замовчуванням", "Әдепкі");
        add("KeepAutostart", "Autostart", "Автозапуск", "Автозапуск", "Автоқосылу");
        add("KeepAutostartDesc", "Only if you turn it on: after the phone restarts or SealGram updates, the background service starts by itself and restarts if the system stops it, so deletions are saved even if you never open the app. It only keeps the connection to Telegram for this feature, uses some battery and shows a permanent notification. Nothing is sent anywhere else. Turn it off to stop it completely.", "Только если ты включишь: после перезагрузки телефона или обновления SealGram фоновая служба запускается сама и перезапускается, если система её остановит, поэтому удалённые сохраняются, даже если ты не открываешь приложение. Она лишь держит соединение с Telegram для этой функции, немного расходует батарею и показывает постоянное уведомление. Ничего никуда больше не отправляется. Выключи, чтобы остановить полностью.", "Лише якщо ти ввімкнеш: після перезавантаження телефона чи оновлення SealGram фонова служба запускається сама й перезапускається, якщо система її зупинить, тому видалені зберігаються, навіть якщо ти не відкриваєш застосунок. Вона лише тримає з’єднання з Telegram для цієї функції, трохи витрачає батарею й показує постійне сповіщення. Нічого більше нікуди не надсилається. Вимкни, щоб зупинити повністю.", "Тек сіз қоссаңыз: телефон қайта қосылғаннан немесе SealGram жаңартылғаннан кейін фондық қызмет өзі іске қосылады және жүйе тоқтатса, қайта қосылады, сондықтан қолданбаны ашпасаңыз да жойылғандар сақталады. Ол осы функция үшін Telegram-мен байланысты ғана ұстап тұрады, батареяны аздап жұмсайды және тұрақты хабарландыру көрсетеді. Басқа ешқайда ештеңе жіберілмейді. Толық тоқтату үшін өшіріңіз.");
        add("KeepNotifTitle", "SealGram is running in the background", "SealGram работает в фоне", "SealGram працює у фоні", "SealGram фонда жұмыс істеп тұр");
        add("KeepNotifText", "Saving deleted messages. Turn it off in the mod settings.", "Сохраняет удалённые сообщения. Выключается в настройках мода.", "Зберігає видалені повідомлення. Вимикається в налаштуваннях мода.", "Жойылған хабарларды сақтайды. Мод баптауларында өшіріледі.");
        add("KeepMore", "More", "Ещё", "Ще", "Тағы");
        add("EditHistory", "Edit history", "История изменений", "Історія змін", "Өзгерту тарихы");
        add("EditHistoryDesc", "Keeps earlier versions of messages that others edit (up to 10 per message, for the same number of days as saved deleted messages). Open a message’s menu to see them. Stored only on this device; off by default.", "Сохраняет прежние версии сообщений, которые другие редактируют (до 10 на сообщение, на тот же срок, что и удалённые). Смотри в меню сообщения. Хранится только на этом устройстве; по умолчанию выключено.", "Зберігає попередні версії повідомлень, які редагують інші (до 10 на повідомлення, на той самий термін, що й видалені). Дивись у меню повідомлення. Зберігається лише на цьому пристрої; за замовчуванням вимкнено.", "Басқалар өңдеген хабарлардың алдыңғы нұсқаларын сақтайды (хабарға 10-ға дейін, жойылғандармен бірдей мерзімге). Хабар мәзірінен қараңыз. Тек осы құрылғыда сақталады; әдепкіде өшірулі.");
        add("HubLazyArchive", "Load archive only when opened", "Грузить архив только при открытии", "Вантажити архів лише при відкритті", "Мұрағатты тек ашқанда жүктеу");
        add("HubLazyArchiveDesc", "Skips fetching archived chats from the server at startup, so Telegram starts faster. They load when you open the archive.", "Не загружает архивные чаты с сервера при запуске, чтобы Telegram стартовал быстрее. Они подгрузятся, когда откроешь архив.", "Не завантажує архівні чати із сервера під час запуску, щоб Telegram стартував швидше. Вони підвантажаться, коли відкриєш архів.", "Іске қосылғанда мұрағатталған чаттарды серверден жүктемейді, сондықтан Telegram тезірек іске қосылады. Мұрағатты ашқанда жүктеледі.");
        add("GhostDelayWait", "Send delay", "Задержка отправки", "Затримка надсилання", "Жіберу кідірісі");
        add("SecN", "%1$d s", "%1$d с", "%1$d с", "%1$d с");
        add("LogTitle", "Deleted and edited", "Удалённые и изменённые", "Видалені та змінені", "Жойылған және өзгертілген");
        add("LogDeleted", "Deleted messages", "Удалённые сообщения", "Видалені повідомлення", "Жойылған хабарлар");
        add("LogEdited", "Edited messages", "Изменённые сообщения", "Змінені повідомлення", "Өзгертілген хабарлар");
        add("LogEmpty", "Nothing saved yet. Turn the options on in “Keep deleted messages”.", "Пока ничего не сохранено. Включи опции в «Сохранять удалённые сообщения».", "Поки нічого не збережено. Увімкни опції в «Зберігати видалені повідомлення».", "Әзірге ештеңе сақталмаған. «Жойылған хабарларды сақтау» ішінде опцияларды қосыңыз.");
        add("LogClearEdits", "Clear edit history", "Очистить историю изменений", "Очистити історію змін", "Өзгерту тарихын тазалау");
        add("KeepOpenChat", "Open chat", "Открыть чат", "Відкрити чат", "Чатты ашу");
        add("VoiceInRound", "In round videos", "В кружках", "У кружечках", "Дөңгелек бейнелерде");
        add("VoiceInRoundDesc", "Change your voice when recording round videos.", "Менять голос при записи кружков.", "Змінювати голос під час запису кружечків.", "Дөңгелек бейне жазғанда дауысты өзгерту.");
        add("HubHideStories", "Hide stories bar", "Скрыть панель историй", "Сховати панель історій", "Тарихтар жолағын жасыру");
        add("HubHideStoriesDesc", "Removes the stories row above the chat list.", "Убирает ряд историй над списком чатов.", "Прибирає ряд історій над списком чатів.", "Чаттар тізімінің үстіндегі тарихтар қатарын жояды.");
        add("HubHidePremium", "Hide Premium promos", "Скрыть рекламу Premium", "Сховати рекламу Premium", "Premium жарнамасын жасыру");
        add("HubHidePremiumDesc", "Hides Premium upsell prompts and badges in the interface.", "Скрывает предложения Premium в интерфейсе.", "Ховає пропозиції Premium в інтерфейсі.", "Интерфейстегі Premium ұсыныстарын жасырады.");
        add("HubConfirmCall", "Confirm before calling", "Подтверждать звонок", "Підтверджувати дзвінок", "Қоңырау алдында растау");
        add("HubConfirmCallDesc", "Ask before starting an audio or video call.", "Спрашивать перед началом аудио- или видеозвонка.", "Питати перед початком аудіо- чи відеодзвінка.", "Аудио немесе бейне қоңырау алдында сұрау.");
        add("CallConfirmTitle", "Start call?", "Начать звонок?", "Почати дзвінок?", "Қоңырау шалу керек пе?");
        add("CallConfirmText", "Do you really want to call?", "Точно позвонить?", "Точно зателефонувати?", "Шынымен қоңырау шалу керек пе?");
        add("CallConfirmYes", "Call", "Позвонить", "Зателефонувати", "Қоңырау шалу");
        add("VoiceTitle", "Voice changer", "Изменение голоса", "Зміна голосу", "Дауысты өзгерту");
        add("VoiceOff", "Off", "Выключено", "Вимкнено", "Өшірулі");
        add("VoiceDeep", "Deep voice", "Низкий голос", "Низький голос", "Жуан дауыс");
        add("VoiceHigh", "High voice", "Высокий голос", "Високий голос", "Жіңішке дауыс");
        add("VoiceChipmunk", "Chipmunk", "Бурундук", "Бурундук", "Тиін");
        add("VoiceMonster", "Monster", "Монстр", "Монстр", "Құбыжық");
        add("VoiceRobot", "Robot", "Робот", "Робот", "Робот");
        add("VoiceAlien", "Alien", "Пришелец", "Прибулець", "Келімсек");
        add("VoiceInCalls", "In calls", "В звонках", "У дзвінках", "Қоңырауларда");
        add("VoiceInCallsDesc", "Change your voice during calls.", "Менять голос во время звонков.", "Змінювати голос під час дзвінків.", "Қоңырау кезінде дауысты өзгерту.");
        add("VoiceInMessages", "In voice messages", "В голосовых", "У голосових", "Дауыстық хабарларда");
        add("VoiceInMessagesDesc", "Change your voice when recording voice messages.", "Менять голос при записи голосовых.", "Змінювати голос під час запису голосових.", "Дауыстық хабар жазғанда дауысты өзгерту.");
        add("VoiceNote", "Works only on your outgoing voice. Pitch shifting is simple and may sound slightly synthetic.", "Работает только на твоём исходящем голосе. Сдвиг тона простой, звук может быть слегка синтетическим.", "Працює лише на твоєму вихідному голосі. Зсув тону простий, звук може бути трохи синтетичним.", "Тек шығыс дауысыңызға әсер етеді.");
        add("HubGhostPrivacy", "Ghost and privacy", "Призрак и приватность", "Привид і приватність", "Көрінбейтін режим және құпиялылық");
        add("HubOther", "Other", "Другое", "Інше", "Басқа");
        add("HubChatTranslation", "Chat translation", "Перевод чатов", "Переклад чатів", "Чаттарды аудару");
        add("HubLinks", "Links", "Ссылки", "Посилання", "Сілтемелер");
        add("HubChannel", "Channel", "Канал", "Канал", "Арна");
        add("HubWebsite", "Website", "Сайт", "Вебсайт", "Веб-сайт");
        add("HubTelegramServices", "Telegram services", "Сервисы Telegram", "Сервіси Telegram", "Telegram қызметтері");
        add("HubOpen", "Open", "Открыть", "Відкрити", "Ашу");
        add("TelegramPremium", "Telegram Premium", "Telegram Premium", "Telegram Premium", "Telegram Premium");
        add("TelegramBusiness", "Telegram Business", "Telegram Business", "Telegram Business", "Telegram Business");
        add("HubChangesRestart", "Some changes apply after restarting the chat or app.", "Часть изменений применится после перезапуска чата или приложения.", "Частина змін застосовується після перезапуску чату або застосунку.", "Кейбір өзгерістер чатты немесе қолданбаны қайта іске қосқаннан кейін қолданылады.");
        add("HubBlockAds", "Block ads", "Блокировать рекламу", "Блокувати рекламу", "Жарнаманы бұғаттау");
        add("HubBlockAdsDesc", "Sponsored messages in channels, bots and search", "Спонсируемые сообщения в каналах, ботах и поиске", "Спонсоровані повідомлення в каналах, ботах і пошуку", "Арналардағы, боттардағы және іздеудегі демеушілік хабарламалар");
        add("HubHidePhone", "Hide my number in profile", "Скрывать мой номер в профиле", "Сховати мій номер у профілі", "Профильде нөмірімді жасыру");
        add("HubHidePhoneDesc", "Only on this device", "Только на этом устройстве", "Лише на цьому пристрої", "Тек осы құрылғыда");
        add("HubProfileInfo", "ID, registration date and DC", "ID, дата регистрации и DC", "ID, дата реєстрації та DC", "ID, тіркелген күні және DC");
        add("HubProfileInfoDesc", "Show a line in the user's profile", "Строка в профиле пользователя", "Рядок у профілі користувача", "Пайдаланушы профилінде көрсету");
        add("HubNoRound", "Do not abbreviate numbers", "Не сокращать числа", "Не округлювати числа", "Сандарды қысқартпау");
        add("HubNoRoundDesc", "Show 1,234 instead of 1.2K", "Показывать 1 234 вместо 1,2K", "Показувати 1,234 замість 1.2K", "1,2K орнына 1 234 көрсету");
        add("HubNoUnarchive", "Disable swipe to unarchive", "Отключить свайп для разархивации", "Без свайпу для розархівації", "Архивтен шығару үшін сырғытуды өшіру");
        add("HubNoUnarchiveDesc", "Disable chat swiping in the archive", "Отключить свайп чатов в архиве", "Вимкнути свайп чатів в архіві", "Архивтегі чаттарды сырғытуды өшіру");
        add("HubNoDividers", "Remove separators", "Убрать разделители", "Прибрати роздільники", "Бөлгіштерді алып тастау");
        add("HubNoDividersDesc", "In settings lists and search", "В списках настроек и поиска", "У списках налаштувань і пошуку", "Параметрлер мен іздеу тізімдерінде");
        add("HubHideShare", "Hide the Share button", "Скрыть кнопку «Поделиться»", "Сховати кнопку «Поділитися»", "«Бөлісу» түймесін жасыру");
        add("HubHideShareDesc", "Next to messages in channels", "Рядом с сообщениями в каналах", "Біля повідомлень у каналах", "Арналардағы хабарламалардың жанында");
        add("HubHideKeyboardScroll", "Hide keyboard when scrolling", "Скрывать клавиатуру при прокрутке", "Ховати клавіатуру під час прокручування", "Сырғытқанда пернетақтаны жасыру");
        add("HubNoNextChannel", "Do not switch to the next channel", "Не переходить к следующему каналу", "Не переходити до наступного каналу", "Келесі арнаға өтпеу");
        add("HubNoNextChannelDesc", "Disable channel switching while scrolling", "Отключить переключение при прокрутке", "Вимкнути перехід під час прокручування", "Сырғыту кезінде арна ауысуын өшіру");
        add("HubCommaAfterMention", "Comma after mentions", "Запятая после упоминания", "Кома після згадки", "Еске алудан кейін үтір");
        add("HubCommaAfterMentionDesc", "Use @username, instead of @username", "Использовать @username, вместо @username", "Використовувати @username, замість @username", "@username орнына @username, пішімін қолдану");
        add("HubCircularVideos", "Round video messages", "Видеокружки", "Кругові відео", "Дөңгелек бейнехабарлар");
        add("HubHoldToSave", "Hold to save", "Удерживай, чтобы сохранить", "Утримуй, щоб зберегти", "Сақтау үшін басып тұрыңыз");
        add("HubVoiceToTextNote", "Recognition runs on this device: the voice message is not played or uploaded for transcription. Tap the transcript button beside a voice message to show text in the chat. Internet is needed only for the one-time model download.", "Распознавание выполняется на устройстве: голосовое не воспроизводится и не отправляется на распознавание. Нажми кнопку текста рядом с голосовым, чтобы увидеть текст прямо в чате. Интернет нужен только для однократной загрузки модели.", "Розпізнавання виконується на пристрої: голосове не відтворюється й не надсилається для розпізнавання. Натисни кнопку тексту біля голосового, щоб побачити текст у чаті. Інтернет потрібен лише для одноразового завантаження моделі.", "Тану құрылғыда орындалады: дауыстық хабарлама ойнатылмайды және тану үшін жіберілмейді. Мәтінді чатта көру үшін дауыстық хабарламаның жанындағы мәтін түймесін басыңыз. Интернет модельді бір рет жүктеу үшін ғана қажет.");
        add("HubAutoReplyNote", "Works while SealGram is running and connected. It sends one reply per period of inactivity. Bots, Telegram service chats and deleted accounts are ignored.", "Работает, пока SealGram запущен и подключён к сети. За один период отсутствия отправляется один ответ. Боты, служебные чаты Telegram и удалённые аккаунты игнорируются.", "Працює, поки SealGram запущений і підключений до мережі. За один період неактивності надсилається одна відповідь. Боти, службові чати Telegram і видалені акаунти ігноруються.", "SealGram іске қосылып, желіге қосылғанда жұмыс істейді. Әр әрекетсіздік кезеңінде бір жауап жібереді. Боттар, Telegram қызметтік чаттары және жойылған аккаунттар еленбейді.");
        add("HubOneTapText", "Transcribe with one tap", "Текст одним нажатием", "Текст одним дотиком", "Бір түртумен мәтінге айналдыру");
        add("HubOneTapTextDesc", "The voice-message transcript button shows text directly in chat without playing audio", "Кнопка расшифровки рядом с голосовым сразу покажет текст в чате, не запуская аудио", "Кнопка розпізнавання біля голосового одразу покаже текст у чаті, не запускаючи аудіо", "Дауыстық хабарлама жанындағы мәтін түймесі аудионы ойнатпай-ақ мәтінді чатта көрсетеді");
        add("HubVoiceMenu", "Offline transcription in voice menu", "Офлайн-расшифровка в меню голосовых", "Пункт у меню голосових", "Дауыстық хабарлама мәзіріндегі офлайн тану");
        add("HubVoiceMenuDesc", "Add “Recognize text (offline)” to the message menu; no Premium required", "Добавляет «Распознать текст (офлайн)» в меню сообщения; Premium не требуется", "Додає «Розпізнати текст (офлайн)» до меню повідомлення; Premium не потрібен", "Хабарлама мәзіріне «Мәтінді офлайн тану» пәрменін қосады; Premium қажет емес");
        add("HubRecognitionModel", "Recognition model", "Модель распознавания", "Модель розпізнавання", "Тану моделі");
        add("HubModelLoading", "Downloading model…", "Загружается модель…", "Завантажується модель…", "Модель жүктелуде…");
        add("HubModelReady", "Model “%1$s” is ready. Tap the transcript button beside a voice message; audio will not play.", "Модель «%1$s» готова. Нажми кнопку текста рядом с голосовым — аудио не будет воспроизводиться.", "Модель «%1$s» готова. Натисни кнопку тексту біля голосового — аудіо не відтворюватиметься.", "«%1$s» моделі дайын. Дауыстық хабарлама жанындағы мәтін түймесін басыңыз — аудио ойнатылмайды.");
        add("HubChooseModel", "Choose a language below. The model is downloaded once and then works offline.", "Выбери язык ниже. Модель нужна один раз и далее работает офлайн.", "Обери мову нижче. Модель потрібна лише один раз і далі працює офлайн.", "Төменнен тілді таңдаңыз. Модель бір рет жүктеледі де, кейін офлайн жұмыс істейді.");
        add("HubSelected", "Selected", "Выбрано", "Обрано", "Таңдалған");
        add("HubInstalled", "Installed", "Установлено", "Встановлено", "Орнатылған");
        add("HubModelDownloadFailed", "Model download failed", "Не удалось загрузить модель", "Не вдалося завантажити модель", "Модельді жүктеу сәтсіз аяқталды");
        add("CommonOK", "OK", "ОК", "Гаразд", "Жарайды");
        add("CommonSave", "Save", "Сохранить", "Зберегти", "Сақтау");
        add("CommonCancel", "Cancel", "Отмена", "Скасувати", "Бас тарту");
        add("HubTranslationNote", "Telegram's built-in real-time translation processes new messages in eligible chats. Automatic translation of an entire chat requires Telegram Premium. Individual message translation can be enabled separately.", "Встроенный перевод Telegram автоматически переводит новые сообщения в подходящих чатах. Автоперевод всего чата требует Telegram Premium. Перевод отдельных сообщений можно включить отдельно.", "Вбудований переклад Telegram автоматично перекладає нові повідомлення у відповідних чатах. Автопереклад усього чату потребує Telegram Premium. Переклад окремих повідомлень можна ввімкнути окремо.", "Telegram-ның кіріктірілген аудармасы қолжетімді чаттардағы жаңа хабарламаларды автоматты аударады. Бүкіл чатты автоматты аудару үшін Telegram Premium қажет. Жеке хабарламаларды аударуды бөлек қосуға болады.");
        add("HubRealTimeTranslation", "Real-time chat translation", "Автоперевод чатов в реальном времени", "Автопереклад чатів у реальному часі", "Чаттарды нақты уақытта автоматты аудару");
        add("HubRealTimeTranslationDesc", "Show Telegram's native translation bar in chats where the feature is available", "Показывать штатную панель перевода в чатах, где функция доступна", "Показувати штатну панель перекладу в чатах, де функція доступна", "Функция қолжетімді чаттарда Telegram аударма жолағын көрсету");
        add("HubTranslateIndividual", "Translate individual messages", "Перевод отдельных сообщений", "Переклад окремих повідомлень", "Жеке хабарламаларды аудару");
        add("HubTranslateIndividualDesc", "Enable the translation button in the message menu where Telegram allows it", "Включить перевод в меню сообщения там, где Telegram его разрешает", "Увімкнути переклад у меню повідомлення там, де Telegram це дозволяє", "Telegram рұқсат еткен жерде хабарлама мәзіріндегі аударманы қосу");
        add("HubPremiumRequiredTitle", "Telegram Premium required", "Требуется Telegram Premium", "Потрібен Telegram Premium", "Telegram Premium қажет");
        add("HubPremiumRequiredMessage", "Telegram requires an active Premium subscription for automatic translation of entire chats. Open the official Telegram purchase screen to subscribe.", "Для автоперевода всего чата Telegram требует активную подписку Premium. Открой официальный экран Telegram, чтобы оформить подписку.", "Для автоперекладу всього чату Telegram потребує активну підписку Premium. Відкрий офіційний екран Telegram, щоб оформити підписку.", "Бүкіл чатты автоматты аудару үшін Telegram белсенді Premium жазылымын талап етеді. Жазылым алу үшін Telegram-ның ресми төлем экранын ашыңыз.");
        add("HubMessages", "Messages", "Сообщения", "Повідомлення", "Хабарламалар");
        add("HubPreviewCaption", "This is how the other person will see it", "Так это увидит собеседник", "Так це побачить співрозмовник", "Мұны әңгімелесуші осылай көреді");
        add("HubAutoReplyBubble", "auto-reply", "автоответ", "автовідповідь", "автожауап");
        add("HubChangeText", "Change text", "Изменить текст", "Змінити текст", "Мәтінді өзгерту");
        add("HubWhenReply", "When to reply", "Когда отвечать", "Коли відповідати", "Қашан жауап беру");
        add("HubIfAwayFor", "If I'm away for more than", "Если меня нет больше", "Якщо мене немає понад", "Мен болмаған уақыт");
        add("HubWhoNot", "Who should not receive replies", "Кому не отвечать", "Кому не відповідати", "Кімге жауап бермеу керек");
        add("HubMyContacts", "My contacts", "Моим контактам", "Моїм контактам", "Контактілерім");
        add("HubMyContactsDesc", "Reply only to people who are not in contacts", "Отвечать только незнакомым", "Відповідати лише незнайомим", "Тек контактілерде жоқ адамдарға жауап беру");
        add("HubRemove", "Remove", "Убрать", "Прибрати", "Жою");
        add("HubAddExclusion", "Add exception", "Добавить исключение", "Додати виключення", "Ерекшелік қосу");
        add("HubEditAutoReplyText", "Auto-reply text", "Текст автоответа", "Текст автовідповіді", "Автожауап мәтіні");
        add("HubMinutes", "min", "мин", "хв", "мин");
        add("Delay15Minutes", "15 minutes", "15 минут", "15 хвилин", "15 минут");
        add("Delay30Minutes", "30 minutes", "30 минут", "30 хвилин", "30 минут");
        add("Delay1Hour", "1 hour", "1 час", "1 година", "1 сағат");
        add("Delay3Hours", "3 hours", "3 часа", "3 години", "3 сағат");
        add("Delay6Hours", "6 hours", "6 часов", "6 годин", "6 сағат");
        add("Delay12Hours", "12 hours", "12 часов", "12 годин", "12 сағат");
        add("Delay24Hours", "24 hours", "24 часа", "24 години", "24 сағат");
        add("AutoReplyDefault", "I'm currently unavailable. I'll reply as soon as I can.", "Сейчас меня нет на связи. Отвечу, как смогу.", "Зараз мене немає на зв'язку. Відповім, щойно зможу.", "Қазір байланыста емеспін. Қолым босаған соң жауап беремін.");
        add("UnitMb", "MB", "МБ", "МБ", "МБ");
        add("SttLangUkrainianEasy", "Ukrainian (small)", "Украинский (компактная модель)", "Українська (легка)", "Українша (шағын модель)");
        add("SttLangUkrainianAccurate", "Ukrainian (more accurate)", "Украинский (точнее)", "Українська (точніша)", "Українша (дәлірек)");
        add("SttLangRussian", "Russian", "Русский", "Російська", "Орыс тілі");
        add("SttLangEnglish", "English", "Английский", "Англійська", "Ағылшын тілі");
        add("SttLangPolish", "Polish", "Польский", "Польська", "Поляк тілі");
        add("SttLangGerman", "German", "Немецкий", "Німецька", "Неміс тілі");
        add("SttNoInternet", "No internet connection", "Нет подключения к интернету", "Немає підключення до інтернету", "Интернетке қосылым жоқ");
        add("SttHttpError", "The server returned HTTP %1$d", "Сервер вернул код HTTP %1$d", "Сервер повернув код HTTP %1$d", "Сервер HTTP %1$d кодын қайтарды");
        add("SttWrongRemoteSize", "The downloaded file has an invalid size", "Загруженный файл имеет неправильный размер", "Завантажений файл має неправильний розмір", "Жүктелген файлдың өлшемі дұрыс емес");
        add("SttDownloading", "Downloading: %1$s", "Загрузка: %1$s", "Завантаження: %1$s", "Жүктелуде: %1$s");
        add("SttDownloadInterrupted", "Download was interrupted", "Загрузка прервана", "Завантаження перервано", "Жүктеу үзілді");
        add("SttFileTooSmall", "The downloaded file is too small", "Загруженный файл слишком маленький", "Завантажений файл замалий", "Жүктелген файл тым кішкентай");
        add("SttCheckingArchive", "Checking archive…", "Проверка архива…", "Перевірка архіву…", "Мұрағат тексерілуде…");
        add("SttArchiveMissingModel", "The archive does not contain the model", "В архиве нет модели", "Архів не містить моделі", "Мұрағатта модель жоқ");
        add("SttUnpacking", "Extracting model…", "Распаковка…", "Розпакування…", "Модель шығарылуда…");
        add("SttModelIncomplete", "The model was not fully extracted", "Модель распакована не полностью", "Модель розпакувалась не повністю", "Модель толық шығарылмады");
        add("SttCheckingModel", "Checking model…", "Проверка модели…", "Перевірка моделі…", "Модель тексерілуде…");
        add("SttSaveFailed", "Could not save the model", "Не удалось сохранить модель", "Не вдалося зберегти модель", "Модельді сақтау мүмкін болмады");
        add("SttErrorPrefix", "Error: ", "Ошибка: ", "Помилка: ", "Қате: ");
        add("SttModelInstalled", "Model installed", "Модель установлена", "Модель встановлено", "Модель орнатылды");
        add("SttDialogTitle", "Voice to text", "Голос в текст", "Голос у текст", "Дауысты мәтінге айналдыру");
        add("SttModelNotInstalled", "The “%1$s” model is not installed yet (%2$s MB, one-time download). Open settings?", "Модель «%1$s» ещё не установлена (%2$s МБ, один раз). Открыть настройки?", "Модель «%1$s» ще не встановлена (%2$s МБ, один раз). Відкрити налаштування?", "«%1$s» моделі әлі орнатылмаған (%2$s МБ, бір рет жүктеледі). Параметрлерді ашу керек пе?");
        add("SttOpen", "Open", "Открыть", "Відкрити", "Ашу");
        add("SttNotFoundLocalVoice", "Could not find a local voice-message file. If it is downloaded, try again; otherwise download the audio in the chat.", "Не найден локальный файл голосового. Если он уже загружен, попробуй ещё раз; иначе загрузи аудио в чате.", "Не знайшов локальний файл голосового. Якщо він уже завантажений, спробуй ще раз; інакше завантаж аудіо в чаті.", "Дауыстық хабарламаның жергілікті файлы табылмады. Ол жүктелген болса, қайта көріңіз; әйтпесе чатта аудионы жүктеңіз.");
        add("SttRecognitionBusy", "Recognition is already running", "Распознавание уже выполняется", "Розпізнавання вже виконується", "Тану қазір орындалып жатыр");
        add("SttRecognizing", "Recognizing…", "Распознаю…", "Розпізнаю…", "Танылуда…");
        add("SttModelNotFoundStorage", "The model was not found in SealGram storage", "Модель не найдена в хранилище SealGram", "Модель не знайдена у сховищі SealGram", "Модель SealGram жадында табылмады");
        add("SttRecognitionFailed", "Could not recognize: %1$s", "Не удалось распознать: %1$s", "Не вдалося розпізнати: %1$s", "Тану мүмкін болмады: %1$s");
        add("SttLanguageUndetected", "Could not recognize the language", "Не удалось распознать речь", "Мову не розпізнано", "Сөйлеу тілі анықталмады");
        add("SttRecognizedText", "Recognized text", "Распознанный текст", "Розпізнаний текст", "Танылған мәтін");
        add("SttCopy", "Copy", "Копировать", "Копіювати", "Көшіру");
        add("SttOfflineTitle", "Offline recognition", "Офлайн-распознавание", "Офлайн-розпізнавання", "Офлайн тану");
        add("SttInlineModelNotFound", "Model “%1$s” was not found. Open SealGram → “Voice to text” and tap the language to install it once.", "Модель «%1$s» не найдена. Открой SealGram → «Голос в текст» и нажми на язык, чтобы установить модель.", "Модель «%1$s» не знайдена. Відкрий SealGram → «Голос у текст» і натисни мову, щоб установити її один раз.", "«%1$s» моделі табылмады. SealGram → «Дауысты мәтінге айналдыру» бөлімін ашып, тілді басып, модельді бір рет орнатыңыз.");
        add("SttUnderstand", "Got it", "Понятно", "Зрозуміло", "Түсінікті");
        add("SttAudioNotFound", "No audio file was found in local storage. Download the voice message in chat and tap the button again.", "Аудиофайл не найден в локальном хранилище. Загрузи голосовое в чате и нажми кнопку ещё раз.", "Аудіофайл не знайдено в локальному сховищі. Завантаж голосове в чаті й натисни кнопку ще раз.", "Жергілікті жадтан аудио табылмады. Дауыстық хабарламаны чатта жүктеп, түймені қайта басыңыз.");
        add("SttAnotherModelLoading", "Another model is already downloading", "Уже загружается другая модель", "Вже завантажується інша модель", "Басқа модель жүктеліп жатыр");
        add("SttAudioTrackMissing", "No audio track", "Нет аудиодорожки", "Немає аудіодоріжки", "Аудиожол жоқ");
        add("SttAudioEmpty", "Audio is empty", "Аудио пустое", "Порожнє аудіо", "Аудио бос");

        add("HubDisclaimer", "SealGram is an unofficial client and is not affiliated with Telegram.", "SealGram — неофициальный клиент, не связанный с Telegram.", "SealGram — неофіційний клієнт, не пов'язаний з Telegram.", "SealGram — Telegram-мен байланысы жоқ бейресми клиент.");
        add("PrivacyTitle", "Ghost mode and privacy", "Режим призрака и приватность", "Привид і приватність", "Көрінбейтін режим және құпиялылық");
        add("PrivacyGhostMode", "Ghost mode", "Режим призрака", "Режим привида", "Көрінбейтін режим");
        add("PrivacyGhostModeDesc", "Hide online status, typing and read receipts. Telegram may still reveal some activity.", "Скрывает онлайн, набор текста и прочтение. Telegram всё равно может показывать часть активности.", "Не показує онлайн, друк і прочитання повідомлень. Telegram може частково видавати активність.", "Желіде болуды, теруді және оқылғаны туралы белгіні жасырады. Telegram кейбір белсенділікті бәрібір көрсете алады.");
        add("PrivacyHideOnline", "Hide online status", "Скрыть статус «в сети»", "Приховати онлайн", "Желіде күйін жасыру");
        add("PrivacyHideOnlineDesc", "Do not update the online status.", "Не обновлять статус «в сети».", "Не оновлювати статус «у мережі».", "«Желіде» күйін жаңартпау.");
        add("PrivacyHideTyping", "Hide “typing…” status", "Скрыть статус «печатает…»", "Приховати «друкує…»", "«Теріп жатыр…» күйін жасыру");
        add("PrivacyHideTypingDesc", "Do not send typing status.", "Не отправлять статус набора текста.", "Не надсилати статус набору тексту.", "Теру күйін жібермеу.");
        add("PrivacyHideRead", "Do not mark messages as read", "Не отмечать сообщения прочитанными", "Не позначати повідомлення прочитаними", "Хабарламаларды оқылған деп белгілемеу");
        add("PrivacyHideReadDesc", "Do not send read receipts.", "Не отправлять подтверждения прочтения.", "Не надсилати підтвердження прочитання.", "Оқылғаны туралы растауды жібермеу.");
        add("PrivacyHidePhone", "Hide my number in profile", "Скрыть мой номер в профиле", "Сховати мій номер у профілі", "Профильде нөмірімді жасыру");
        add("PrivacyHidePhoneDesc", "This only affects this device; Telegram privacy settings are unchanged.", "Только на этом устройстве; настройки приватности Telegram не меняются.", "Лише на цьому пристрої; налаштування приватності Telegram не змінюються.", "Тек осы құрылғыда; Telegram құпиялылық параметрлері өзгермейді.");
        add("ObjectRegistration", "Registration: ~%1$s (approximate)", "Регистрация: ~%1$s (примерно)", "Реєстрація: ~%1$s (орієнтовно)", "Тіркелуі: ~%1$s (шамамен)");
        add("ObjectUnknown", "unknown", "неизвестно", "невідомо", "белгісіз");
        add("ObjectIdLine", "ID: %1$d%2$s  ·  %3$s", "ID: %1$d%2$s  ·  %3$s", "ID: %1$d%2$s  ·  %3$s", "ID: %1$d%2$s  ·  %3$s");
        add("ObjectTapToCopy", "tap to copy", "нажми, чтобы скопировать", "торкнись, щоб скопіювати", "көшіру үшін түртіңіз");
        add("ObjectInspectorTitle", "Object information", "Информация об объекте", "Інформація про об'єкт", "Нысан туралы ақпарат");
        add("ObjectCopyJson", "Copy JSON", "Копировать JSON", "Копіювати JSON", "JSON көшіру");
        add("ObjectCopyId", "Copy ID", "Копировать ID", "Копіювати ID", "ID көшіру");
        add("ObjectClose", "Close", "Закрыть", "Закрити", "Жабу");
        add("MessageMediaOther", "Text / other", "Текст / другое", "Текст / інше", "Мәтін / басқа");
        add("MessageMediaDocument", "Document", "Документ", "Документ", "Құжат");
        add("MessageMediaVideo", "Video", "Видео", "Відео", "Бейне");
        add("MessageMediaAudio", "Audio", "Аудио", "Аудіо", "Аудио");
        add("MessageMediaSticker", "Sticker", "Стикер", "Стікер", "Стикер");
        add("MessageMediaPhoto", "Photo", "Фото", "Фото", "Фото");
        add("MessageTechnicalDetails", "Technical details", "Технические данные", "Технічні дані", "Техникалық мәліметтер");
        add("MessageDetailsTitle", "Message details", "Данные сообщения", "Дані повідомлення", "Хабарлама мәліметтері");
        add("MessageDetailsCopy", "Copy details", "Скопировать данные", "Копіювати дані", "Мәліметтерді көшіру");
        add("MessageDetailsCopied", "Technical details copied", "Технические данные скопированы", "Технічні дані скопійовано", "Техникалық мәліметтер көшірілді");
        add("MessageDetailsId", "Message ID: %1$d", "ID сообщения: %1$d", "ID повідомлення: %1$d", "Хабарлама ID: %1$d");
        add("MessageDetailsChatId", "Chat ID: %1$d", "ID чата: %1$d", "ID чату: %1$d", "Чат ID: %1$d");
        add("MessageDetailsDate", "Date: %1$s", "Дата: %1$s", "Дата: %1$s", "Күні: %1$s");
        add("MessageDetailsMedia", "Media: %1$s", "Медиа: %1$s", "Медіа: %1$s", "Медиа: %1$s");
        add("MessageDetailsMime", "MIME: %1$s", "MIME: %1$s", "MIME: %1$s", "MIME: %1$s");
        add("MessageDetailsSize", "Size: %1$d bytes", "Размер: %1$d байт", "Розмір: %1$d байт", "Өлшемі: %1$d байт");
        add("MessageDetailsDimensions", "Dimensions: %1$d × %2$d", "Разрешение: %1$d × %2$d", "Роздільність: %1$d × %2$d", "Ажыратымдылығы: %1$d × %2$d");
        add("MessageDetailsReply", "Reply to message ID: %1$d", "Ответ на сообщение ID: %1$d", "Відповідь на повідомлення ID: %1$d", "Жауап хабарлама ID: %1$d");
        add("MessageDetailsForwarded", "Forwarded: %1$s", "Переслано: %1$s", "Переслано: %1$s", "Жіберілген: %1$s");
        add("MessageDetailsYes", "Yes", "Да", "Так", "Иә");
        add("MessageDetailsNo", "No", "Нет", "Ні", "Жоқ");
        add("GhostShortcut", "Ghost mode", "Режим призрака", "Режим привида", "Елес режимі");
        add("GhostToggledOn", "Ghost mode on", "Режим призрака включён", "Режим привида увімкнено", "Елес режимі қосулы");
        add("GhostToggledOff", "Ghost mode off", "Режим призрака выключен", "Режим привида вимкнено", "Елес режимі өшірулі");
        add("HubTimeSeconds", "Seconds in message time", "Секунды во времени сообщений", "Секунди в часі повідомлень", "Хабарлама уақытындағы секундтар");
        add("HubTimeSecondsDesc", "Shows the exact send time down to the second in every message bubble. Reopen the chat to apply.", "Показывает точное время отправки до секунды в каждом сообщении. Переоткрой чат, чтобы применить.", "Показує точний час надсилання до секунди в кожному повідомленні. Перевідкрий чат, щоб застосувати.", "Әр хабарламада жіберу уақытын секундқа дейін көрсетеді. Қолдану үшін чатты қайта ашыңыз.");
        add("MessageDetailsSender", "Sender ID: %1$d", "ID отправителя: %1$d", "ID відправника: %1$d", "Жіберуші ID: %1$d");
        add("MessageDetailsEdited", "Edited: %1$s", "Изменено: %1$s", "Змінено: %1$s", "Өзгертілген: %1$s");
        add("MessageDetailsFwdDate", "Original date: %1$s", "Оригинальная дата: %1$s", "Оригінальна дата: %1$s", "Түпнұсқа күні: %1$s");
        add("MessageDetailsViews", "Views: %1$d, forwards: %2$d", "Просмотры: %1$d, пересылки: %2$d", "Перегляди: %1$d, пересилання: %2$d", "Қаралым: %1$d, жіберу: %2$d");
        add("MessageDetailsVia", "Via bot ID: %1$d", "Через бота ID: %1$d", "Через бота ID: %1$d", "Бот ID арқылы: %1$d");
        add("MessageDetailsGroup", "Album ID: %1$d", "ID альбома: %1$d", "ID альбому: %1$d", "Альбом ID: %1$d");
        add("MessageDetailsDc", "Data center: DC%1$d", "Дата-центр: DC%1$d", "Дата-центр: DC%1$d", "Дата-орталық: DC%1$d");
        add("HubRegexEnabled", "Hide messages by regex", "Скрывать сообщения по regex", "Ховати повідомлення за regex", "Хабарламаларды regex бойынша жасыру");
        add("HubRegexEnabledDesc", "Messages that match your patterns collapse to nothing. Works in channels by default.", "Сообщения, подходящие под шаблоны, схлопываются. По умолчанию работает в каналах.", "Повідомлення, що підходять під шаблони, згортаються. За замовчуванням працює в каналах.", "Үлгіге сәйкес хабарламалар жасырылады. Әдепкіде арналарда жұмыс істейді.");
        add("HubRegexChats", "Also in groups and private chats", "Также в группах и личных чатах", "Також у групах та особистих чатах", "Топтар мен жеке чаттарда да");
        add("HubRegexChatsDesc", "Off: only channels are filtered.", "Выключено: фильтруются только каналы.", "Вимкнено: фільтруються лише канали.", "Өшірулі: тек арналар сүзіледі.");
        add("HubRegexFilters", "Regex patterns", "Regex-шаблоны", "Regex-шаблони", "Regex үлгілері");
        add("RegexEditHint", "One regex per line", "Один regex на строку", "Один regex на рядок", "Әр жолға бір regex");
        add("MarkDeleted", "Deleted mark", "Метка «удалено»", "Мітка «видалено»", "«Жойылған» белгісі");
        add("MarkEdited", "Edited mark", "Метка «изменено»", "Мітка «змінено»", "«Өзгертілген» белгісі");
        add("MarkDefault", "Default", "По умолчанию", "За замовчуванням", "Әдепкі");
        add("MarkReset", "Reset", "Сбросить", "Скинути", "Қалпына келтіру");
        add("ReadAfterSend", "Read after sending", "Прочитано после отправки", "Прочитано після надсилання", "Жібергеннен кейін оқылды");
        add("ReadAfterSendDesc", "In ghost mode, sending a message in a chat marks it as read, like in AyuGram.", "В режиме призрака отправка сообщения в чате отмечает его прочитанным, как в AyuGram.", "У режимі привида надсилання повідомлення в чаті позначає його прочитаним, як в AyuGram.", "Елес режимінде чатқа хабарлама жіберу оны оқылған деп белгілейді, AyuGram сияқты.");
        add("ChatJumpToStart", "Go to earliest available message", "К самому раннему доступному сообщению", "До найранішого доступного повідомлення", "Ең ерте қолжетімді хабарламаға өту");
        add("AdminTools", "Admin tools", "Инструменты администратора", "Інструменти адміністратора", "Әкімші құралдары");
        add("AdminToolsTitle", "Chat moderation", "Модерация чата", "Модерація чату", "Чатты басқару");
        add("AdminToolsAdmins", "Administrators", "Администраторы", "Адміністратори", "Әкімшілер");
        add("AdminToolsBlocked", "Blocked / restricted users", "Заблокированные и ограниченные", "Заблоковані та обмежені", "Бұғатталған және шектелген");
        add("AdminToolsMembers", "Members", "Участники", "Учасники", "Қатысушылар");
        add("AdminToolsNoRights", "You don't have the required admin rights.", "Недостаточно прав администратора.", "Недостатньо прав адміністратора.", "Әкімші құқықтары жеткіліксіз.");
        add("HubUpdates", "Updates", "Обновления", "Оновлення", "Жаңартулар");
        add("HubUpdateChannel", "SealGram update channel", "Канал обновлений SealGram", "Канал оновлень SealGram", "SealGram жаңарту арнасы");
        add("HubUpdateChannelDesc", "Release notes and APK downloads are posted in this Telegram channel. Downloads are manual; SealGram does not contact an update server or install APKs automatically.", "Описание изменений и APK публикуются в этом Telegram-канале. Обновление вручную: SealGram не обращается к серверу обновлений и не устанавливает APK автоматически.", "Опис змін і APK публікуються в цьому Telegram-каналі. Оновлення вручну: SealGram не звертається до сервера оновлень і не встановлює APK автоматично.", "Өзгерістер сипаттамасы мен APK осы Telegram арнасында жарияланады. Жаңарту қолмен орындалады: SealGram жаңарту серверіне қосылмайды және APK-ны автоматты түрде орнатпайды.");
    }

    private SealGramI18n() {
    }

    private static void add(String key, String en, String ru, String uk, String kk) {
        STRINGS.put(key, new String[] { en, ru, uk, kk });
    }

    private static int languageIndex() {
        try {
            LocaleController.LocaleInfo info = LocaleController.getInstance().getCurrentLocaleInfo();
            String code = info != null ? info.shortName : null;
            if (code != null) {
                code = code.toLowerCase(Locale.ROOT);
                if (code.startsWith("ru")) return 1;
                if (code.startsWith("uk") || code.startsWith("ua")) return 2;
                if (code.startsWith("kk") || code.startsWith("kz")) return 3;
            }
        } catch (Throwable ignore) {
        }
        return 0;
    }

    private static final Map<String, Map<String, String>> EXTRA = new HashMap<>();

    private static String extraCode() {
        try {
            LocaleController.LocaleInfo info = LocaleController.getInstance().getCurrentLocaleInfo();
            String code = info != null ? info.shortName : null;
            if (code == null) return null;
            code = code.toLowerCase(Locale.ROOT);
            for (String c : new String[] {"es", "de", "fr", "pt", "tr", "pl"}) {
                if (code.startsWith(c)) return c;
            }
        } catch (Throwable ignore) {
        }
        return null;
    }

    private static synchronized Map<String, String> loadExtra(String code) {
        Map<String, String> map = EXTRA.get(code);
        if (map != null) return map;
        map = new HashMap<>();
        try (java.io.BufferedReader r = new java.io.BufferedReader(new java.io.InputStreamReader(
                ApplicationLoader.applicationContext.getAssets().open("sealgram_i18n_" + code + ".txt"), "UTF-8"))) {
            String line;
            while ((line = r.readLine()) != null) {
                int eq = line.indexOf('=');
                if (eq > 0) map.put(line.substring(0, eq), line.substring(eq + 1));
            }
        } catch (Throwable ignore) {
        }
        EXTRA.put(code, map);
        return map;
    }

    public static String t(String key) {
        String extra = extraCode();
        if (extra != null) {
            String v = loadExtra(extra).get(key);
            if (v != null && !v.isEmpty()) return v;
        }
        String[] values = STRINGS.get(key);
        if (values == null) return key;
        String value = values[languageIndex()];
        return value == null || value.isEmpty() ? values[0] : value;
    }

    public static String format(String key, Object... args) {
        try {
            Locale locale = LocaleController.getInstance().getCurrentLocale();
            return String.format(locale != null ? locale : Locale.getDefault(), t(key), args);
        } catch (Throwable ignore) {
            return t(key);
        }
    }

    public static String[] autoReplyDelayNames() {
        return new String[] {
            t("Delay15Minutes"), t("Delay30Minutes"), t("Delay1Hour"),
            t("Delay3Hours"), t("Delay6Hours"), t("Delay12Hours"), t("Delay24Hours")
        };
    }
}
