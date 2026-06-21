# QuickShop-PriceGuide Addon

![Build Status](https://github.com/xydesu/QuickShopPriceGuide/actions/workflows/build.yml/badge.svg)
![Release Status](https://github.com/xydesu/QuickShopPriceGuide/actions/workflows/release.yml/badge.svg)

QuickShop-PriceGuide 是一個專為 [QuickShop-Hikari](https://github.com/QuickShop-Community/QuickShop-Hikari) 設計的伺服器擴充套件 (Addon)。當玩家在您的伺服器上建立商店時，它會根據您提供的市場經濟資料表，自動在聊天框提示推薦的「收購」與「出售」價格，讓伺服器經濟更穩定、新手開店更輕鬆！

## 🌟 核心功能

- **建立商店即時提示**：當玩家拿著物品點擊箱子準備開店時，系統會自動給予單個與整組的推薦價格。
- **Auto-Fill 快捷指令**：玩家只需在提示輸入價格時打上 `auto` 或 `rec`，系統就會自動套用推薦的出售價格，一鍵開店！
- **熱更新支援**：輸入 `/priceguide reload` 即可在不重啟伺服器的情況下更新價格表。
- **主動查詢功能**：輸入 `/priceguide [物品]` 即可隨時隨地查閱市價。
- **稅率系統整合**：可在設定檔中設定交易稅金（如 5%），系統給予推薦價格時會自動溢價以防玩家虧本。

---

## 🛠️ 服主安裝與設定指南

### 1. 安裝需求
- Minecraft 伺服器 (1.20+)
- 已安裝 [QuickShop-Hikari](https://github.com/QuickShop-Community/QuickShop-Hikari) (前置插件)

### 2. 安裝步驟
1. 進入本專案的 [Releases 頁面](https://github.com/xydesu/QuickShopPriceGuide/releases)，下載最新版的 `QuickShopPriceGuide-x.x.x.jar`。
2. 將 `.jar` 檔案放入您伺服器的 `plugins/` 資料夾中。
3. 啟動伺服器，讓外掛生成設定檔。
4. 前往 [Minecraft Economy Price Guide](https://minecraft-economy-price-guide.net/) 網站，下載您的市場價格 JSON 資料表。
5. 將下載的檔案重新命名為 `prices.json`，並放入 `plugins/QuickShopPriceGuide/` 目錄下。
6. 在遊戲內輸入 `/priceguide reload` 或重啟伺服器，即可開始使用！

### 3. 設定檔說明 (`config.yml`)
在 `plugins/QuickShopPriceGuide/config.yml` 中，您可以自訂乘數、稅率與所有提示訊息：

```yaml
multipliers:
  sell: 1.0   # 推薦出售價的倍率 (預設 1.0)
  buy: 1.0    # 推薦收購價的倍率 (預設 1.0)

settings:
  tax_rate: 0.05 # 例如 5% 的稅，推薦出售價格會自動乘以 1.05

messages:
  # 您可以自由修改提示的顏色代碼與文字
  recommendation_unit: "&a[系統] 單個推薦 - 出售: $%sell% | 收購: $%buy%"
  ...
```

---

## 💻 開發者指南

本專案使用 Maven 進行依賴管理與編譯。若您想自行編譯或貢獻程式碼：

1. 複製本專案：`git clone https://github.com/USERNAME/QuickShopPriceGuide.git`
2. 進入資料夾：`cd QuickShopPriceGuide`
3. 使用 Maven 編譯：`mvn clean package`
4. 編譯出的 `.jar` 檔案會位於 `target/` 目錄中。

歡迎提交 Pull Request 或建立 Issues！

## 📄 授權條款 (License)
本專案採用 [MIT License](LICENSE) 授權。
