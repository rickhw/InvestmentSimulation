# ETF 投資模擬器

這是一個使用 Java Swing 編寫的桌面應用程式，用於模擬 ETF 的長期定期定額投資表現。使用者可以根據歷史數據，設定不同的投資參數，並以視覺化的方式觀察模擬的投資結果。

## 主要功能

- **定期定額模擬**:
  - 設定模擬的 ETF 標的 (例如: 0050, 00999)。
  - 自訂模擬的起始年/月。
  - 自訂初始投入本金 (單位: 萬)。
  - 自訂每月定期定額投入的金額 (單位: 萬)。

- **視覺化圖表**:
  - 以折線圖呈現「總資產」與「總投入成本」的增長曲線，方便比較。
  - X 軸會動態顯示日期標籤，避免在長期模擬中標籤過於擁擠。

- **詳細數據表格**:
  - **模擬結果**: 按月顯示詳細的投入金額、累積成本、當月績效及資產淨值。表格中的數值靠右對齊，方便閱讀。
  - **歷史績效**: 在右方面板獨立顯示所選 ETF 的完整歷史月績效。

- **多 ETF 支援**:
  - 系統會自動讀取 `data` 資料夾中的 `*.csv` 檔案，動態新增可選擇的 ETF 標的。
  - 可在模擬區和歷史績效區分別選擇不同的 ETF 進行分析。

- **條件格式化**:
  - 在歷史績效表格中，儲存格會根據績效好壞顯示不同顏色：
    - **負績效**: <span style="background-color:#FFDDDD;">淡紅色底</span>
    - **績效 > 5%**: <span style="background-color:#DDFFDD;">淡綠色底</span>
    - **績效 > 10%**: <span style="background-color:#DDFFDD;">淡綠色底</span> + **粗體**

## 如何編譯與執行

本專案現在支援兩種模式：**Web 模式** 和 **Desktop (Swing) 模式**。

專案使用 Gradle 進行建置與管理，並透過 Gradle Wrapper 確保建置環境的一致性，不需手動安裝 Gradle。

1.  **賦予 Wrapper 執行權限** (若尚未設定):
    ```bash
    chmod +x gradlew
    ```

2.  **執行應用程式**：

    您可以使用以下指令啟動應用程式。預設會啟動 Web 模式。

    *   **啟動 Web 模式 (預設)**：
        這會啟動 Spring Boot 內嵌的 Web 伺服器，您可以在瀏覽器中訪問應用程式。
        ```bash
        ./gradlew bootRun
        # 或者明確指定 Web 模式
        ./gradlew bootRun --args='--app.mode=web'
        ```
        啟動後，請在瀏覽器打開 `http://localhost:8080`

    *   **啟動 Desktop (Swing) 模式**：
        這將會啟動原有的 Swing 桌面應用程式，且**不會**啟動 Web 伺服器。
        ```bash
        ./gradlew bootRun --args='--app.mode=desktop'
        ```

    Gradle 會自動處理所有依賴、編譯與執行的任務。

## 專案結構

程式碼遵循標準的 Gradle 專案結構與 MVC (Model-View-Controller) 架構：

- `src/main/java/`: 存放所有 Java 原始碼。
  - `controller/`: 控制器，連接模型與視圖。
  - `model/`: 模型，處理數據與業務邏輯。
  - `view/`: 視圖，所有 GUI 元件。
- `src/main/resources/`: 存放如 `.csv` 等資源檔案。

## 數據檔案

- 所有 ETF 歷史績效數據都存放在 `src/main/resources/` 資料夾中。
- 若要新增 ETF，只需將對應的 CSV 檔案放入此資料夾，然後在 `controller/AppController.java` 中將檔名加入到 `loadAllEtfData` 的參數列表即可。
- CSV 檔案格式應為：第一行為表頭，第一欄為年份，後續 12 欄為各月份的績效百分比。

