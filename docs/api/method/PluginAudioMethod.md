# 音频方法

::: warning 警告
本文档适用于 WAuxiliary 最新版本
:::

## mp3 到 Silk

```beanshell
int mp3ToSilk(String mp3Path, String silkPath);

int mp3ToSilk(String mp3Path, String silkPath, int hz);
```

## silk 到 Mp3

```beanshell
int silkToMp3(String silkPath, String mp3Path);

int silkToMp3(String silkPath, String mp3Path, int hz);
```

## 取毫秒时长

```beanshell
long getDuration(String filePath);
```
