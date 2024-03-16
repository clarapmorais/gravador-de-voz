import javax.sound.sampled.*;
import java.io.*;
import java.util.Scanner;

public class VoiceRecorder {
    // Método para iniciar a gravação
    public static void startRecording(String fileName, int durationInSeconds, AudioFormat format, Mixer.Info mixerInfo) throws IOException {
        // Inicializa o mixer para o dispositivo de áudio selecionado
        Mixer mixer = AudioSystem.getMixer(mixerInfo);

        // Configuração do formato de áudio
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        // Inicializa a captura de áudio
        TargetDataLine line;
        try {
            line = (TargetDataLine) mixer.getLine(info);
            line.open(format);
            line.start();

            // Captura o áudio e grava no arquivo
            AudioInputStream ais = new AudioInputStream(line);
            AudioSystem.write(new AudioInputStream(ais, format, durationInSeconds * (int) format.getSampleRate()), AudioFileFormat.Type.WAVE, new File(fileName + ".wav"));

            // Exibe uma mensagem quando a gravação é concluída
            System.out.println("Gravação finalizada: " + fileName + ".wav");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            if (line != null) {
                line.stop();
                line.close();
            }
        }
    }

    // Método para exibir e permitir que o usuário selecione um mixer de gravação de áudio
    public static Mixer.Info selectMixer() {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        System.out.println("Dispositivos de áudio disponíveis:");
        for (int i = 0; i < mixerInfos.length; i++) {
            System.out.println(i + 1 + ". " + mixerInfos[i].getName());
        }
        System.out.print("Selecione o dispositivo de áudio para gravação: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        return mixerInfos[choice - 1];
    }

    // Método para definir o formato de áudio
    public static AudioFormat selectAudioFormat() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecione o formato de áudio:");
        System.out.println("1. 16-bit PCM");
        System.out.println("2. 8-bit PCM");
        System.out.print("Escolha uma opção: ");
        int choice = scanner.nextInt();
        AudioFormat format = null;
        switch (choice) {
            case 1:
                format = new AudioFormat(16000, 16, 1, true, false);
                break;
            case 2:
                format = new AudioFormat(16000, 8, 1, true, false);
                break;
            default:
                System.out.println("Opção inválida. Usando formato padrão (16-bit PCM).");
                format = new AudioFormat(16000, 16, 1, true, false);
                break;
        }
        return format;
    }

    // Método para solicitar nome do arquivo de saída
    public static String getFileName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do arquivo de saída (sem extensão .wav): ");
        String fileName = scanner.nextLine();
        return fileName;
    }

    // Método para solicitar duração da gravação
    public static int getDurationInSeconds() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite a duração da gravação (em segundos): ");
        int durationInSeconds = scanner.nextInt();
        return durationInSeconds;
    }

    // Método principal
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Iniciando gravação de áudio...");
        
        String fileName = getFileName();
        int durationInSeconds = getDurationInSeconds();
        AudioFormat format = selectAudioFormat();
        Mixer.Info mixerInfo = selectMixer();

        // Inicia a gravação
        try {
            startRecording(fileName, durationInSeconds, format, mixerInfo);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}


