
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
 
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class Paint extends JFrame{
	JPanel gui_panel, paint_panel; 
    // 현 그림판 프레임은 GUI구성 패널, 그려지는 패널로 구성
    
    JButton pencil_bt, eraser_bt; // 연필,지우개 도구를 선택하는 버튼
    JButton colorSelect_bt; // 색선택 버튼
    
    JLabel thicknessInfo_label; // 도구굵기 라벨
    
    JTextField thicknessControl_tf; // 도구굵기가 정해질 텍스트필드
    
    Color selectedColor; 
    // 현 변수에 컬러가 저장되어 추후에 펜색상을 정해주는 변수의 매개변수로 사용된다.
    
    Graphics graphics; // Graphics2D 클래스의 사용을 위해 선언
    Graphics2D g;
    // Graphics2D는 쉽게 말해 기존 graphics의 상위버전이라고 생각하시먄 됩니다.
    
    int thickness = 10; // 현 변수는 그려지는 선의 굴기를 변경할때 변경값이 저장되는 변수
    int startX; // 마우스클릭시작의 X좌표값이 저장될 변수
    int startY; // 마우스클릭시작의 Y좌표값이 저장될 변수
    int endX; // 마우스클릭종료의 X좌표값이 저장될 변수
    int endY; // 마우스클릭종료의 Y좌표값이 저장될 변수
    private int colorCombo,radio;  //색과 라디오 버튼 인덱스 값을 저장

    boolean tf = false; 
    
    public Paint() { // Paint클래스의 디폴트(Default)생성자로 기본적인 GUI구성을 해주는 역할을 한다.
        setLayout(null); // 기본 프레임의 레이아웃을 초기화 시켜 패널을 개발자가 직접 다룰수 있게 됨
        setTitle("HONGTTALK 그림판"); // 프레임 타이틀 지정
        setSize(900,750); // 프레임 사이즈 지정
        setLocationRelativeTo(null); // 프로그램 실행시 화면 중앙에 출력
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        // 프레임 우측상단에 X버튼을 눌렀을떄의 기능 정의
       
        
        gui_panel = new JPanel(); // 프레임 상단에 버튼, 텍스트필드, 라벨등이 UI가 들어갈 패널
        gui_panel.setBackground(Color.YELLOW); // 패널의 배경색을 회색으로 지정
        gui_panel.setLayout(null); 
        // gui_panel의 레이아웃을 null지정하여 컴포넌트들의 위치를 직접 지정해줄수 있다.
        
        pencil_bt = new JButton("연필"); // 연필 버튼 생성
        pencil_bt.setFont(new Font("함초롱돋움", Font.BOLD, 25)); // 버튼 폰트및 글씨 크기 지정
        pencil_bt.setBackground(Color.LIGHT_GRAY); // 연필버튼 배경색 밝은회색으로 지정
        eraser_bt = new JButton("지우개"); // 지우개 버튼 생성
        eraser_bt.setFont(new Font("함초롱돋움", Font.BOLD, 25)); // 버튼 폰트및 글씨 크기  지정
        eraser_bt.setBackground(Color.WHITE);  // 지우개 버튼 배경색 희색으로 지정
        colorSelect_bt = new JButton("선색상"); // 선색상 버튼 생성
        colorSelect_bt.setBackground(Color.PINK); // 선색상 버튼 배경색 분홍색으로 지정
        
        
        thicknessInfo_label = new JLabel("도구굵기"); 
        // 도구굴기 라벨 지정 / 밑에서 나올 텍스트필드의 역할을 설명
        thicknessInfo_label.setFont(new Font("함초롬돋움", Font.BOLD, 20));
        // 도구굵기 라벨 폰트및 글씨 크기 지정
        
        thicknessControl_tf = new JTextField("10", 5); // 도구굵기 입력 텍스트필드 생성
        thicknessControl_tf.setHorizontalAlignment(JTextField.CENTER); 
          // 텍스트필드 라인에 띄어지는 텍스트 중앙 정렬
        thicknessControl_tf.setFont(new Font("궁서체", Font.PLAIN, 25)); 
          // 텍스트필드 X길이 및 폰트 지정
        
        pencil_bt.setBounds(10,10,90,55); // 연필위치
        eraser_bt.setBounds(105,10,109,55); // 위치
        colorSelect_bt.setBounds(785,10,90,55); // 위치
        thicknessInfo_label.setBounds(640,10,100,55); // 굵기위치
        thicknessControl_tf.setBounds(720,22,50,35); //굵기위치
        
        gui_panel.add(pencil_bt); // gui_panel에 연필 버튼 추가
        gui_panel.add(eraser_bt); // gui_panel에 지우개 버튼 추가
        gui_panel.add(colorSelect_bt); // gui_panel에 선색상 버튼 추가
        gui_panel.add(thicknessInfo_label); // gui_panel에 도구굵기 라벨 추가
        gui_panel.add(thicknessControl_tf); // gui_panel에 도구굵기 텍스트필드 추가
        
        gui_panel.setBounds(0,0,900,75); // gui_panel이 프레임에 배치될 위치 지정
        
        ////////////////////////////////////////////////// ↑ 패널 구분 ↓
        
        paint_panel = new JPanel(); // 그림이 그려질 패널 생성
        paint_panel.setBackground(Color.WHITE); // 패널의 배경색 하얀색
        paint_panel.setLayout(null); 
        // paint_panel의 레이아웃을 null해줘 패널 자체를 setBounds로 위치를 조정할수 있다.
        
        paint_panel.setBounds(0,90,885,620); // paint_panel의 위치 조정
        
        add(gui_panel); // 메인프레임에 gui패널 추가 - 위치는 위에서 다 정해줌
        add(paint_panel); // 메인프레임에 paint패널 추가 - 위치는 위에서 다 정해줌
        
        setVisible(true); // 메인프레임을 보이게 한다.
        
        graphics = getGraphics(); // 그래픽초기화
        g = (Graphics2D)graphics; 
        // 기존의 graphics변수를 Graphics2D로 변환후 Graphics2D에 초기화
        // 일반적인 Graphics가 아닌 Graphics2D를 사용한 이유는 펜의 굴기와 관련된 기능을
        //수행하기 위하여 Graphics2D 클래스를 객체화함
        g.setColor(selectedColor); 
        // 그려질 선(=선도 그래픽)의 색상을 selectedColor의 값으로 설정*/
        
        /////////////////////////////////////////////////// ↓ 액션 처리부분
        
        paint_panel.addMouseListener(new MouseListener() { 
            // paint_panel에서의 MouseListener 이벤트 처리
            public void mousePressed(MouseEvent e) { 
            // paint_panel에 마우스 눌림의 액션이 있을떄 밑 메소드 실행
                startX = e.getX(); // 마우스가 눌렸을때 그때의 X좌표값으로 초기화
                startY = e.getY(); // 마우스가 눌렸을때 그때의 Y좌표값으로 초기화
            }
            public void mouseClicked(MouseEvent e) {} // 클릭이벤트 처리
            public void mouseEntered(MouseEvent e) {} // paint_panel범위 내에 진입시 이벤트 처리
            public void mouseExited(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
        });
        paint_panel.addMouseMotionListener(new PaintDraw());
          // paint_panel에 마우스 모션리스너 추가
        pencil_bt.addActionListener(new ToolActionListener()); // 연필버튼 액션처리
        eraser_bt.addActionListener(new ToolActionListener()); // 지우개버튼 액션처리
        colorSelect_bt.addActionListener(new ActionListener() {
          // 선색상버튼 액션처리를 익명클래스로 작성
            public void actionPerformed(ActionEvent e) { // 오버라이딩
                tf = true; 
                JColorChooser chooser = new JColorChooser(); // JColorChooser 클래스객체화
                selectedColor = chooser.showDialog(null, "Color", Color.ORANGE); 
                // selectedColor에 선택된색으로 초기화
                g.setColor(selectedColor);
                        // 그려지는 펜의 색상을 selectedColor를 매개변수로 하여 지정
            }
        });
    }
    
    public class PaintDraw implements MouseMotionListener {
        // 위에서 paint_panel에 MouseMotionListener액션 처리가 될때 현 클래스로 넘어와서 밑 문장을 실행

        @Override
        public void mouseDragged(MouseEvent e) { 
            // paint_panel에서 마우스 드래그 액션이처리될떄 밑 메소드 실행
            thickness = Integer.parseInt(thicknessControl_tf.getText());
            // 텍스트필드부분에서 값을 값고와 thickness변수에 대입
            
                endX = e.getX(); 
                // 드래그 되는 시점에서 X좌표가 저장 - 밑에서 시작좌표와 끝좌표를 연결 해주어 선이 그어지게된다.

                endY = e.getY(); 
               // 드래그 되는 시점에서 Y좌표가 저장 - 밑에서 시작좌표와 끝좌표를 연결 해주어 선이 그어지게된다.

 
                g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND,0)); //선굵기
                g.drawLine(startX+10, startY+121, endX+10, endY+121); // 라인이 그려지게 되는부분        
                
                startX = endX; 
                        // 시작부분이 마지막에 드래그된 X좌표로 찍혀야 다음에 이어 그려질수 있다.
                startY = endY;
                        // 시작부분이 마지막에 드래그된 Y좌표로 찍혀야 다음에 이어 그려질수 있다.
        }
        @Override
        public void mouseMoved(MouseEvent e) {}


    }
    
    public class ToolActionListener implements ActionListener {
        // 연필,지우개 버튼의 액션처리시 실행되는 클래스
        public void actionPerformed(ActionEvent e ) {
            // 오버라이딩된 actionPerformed메소드 실행
            if(e.getSource() == pencil_bt) { // 연필버튼이 눌렸을떄 밑 if문장 블록범위내 문장 실행
                if(tf == false) g.setColor(Color.BLACK); // 그려지는 색상을 검은색 지정
                else g.setColor(selectedColor);  // 그려지는 색상을 selectedColor변수의 값으로 지정
            } else if(e.getSource() == eraser_bt) {
                 // 지우개버튼이 눌렸을떄 밑 if문장 블록범위내 문장 실행
                g.setColor(Color.WHITE);
            // 그려지는 색상을 흰색으로 해줬기 때문에 흰색으로 펜이 그려져 지워지는 것처럼 보이게 한다.

            }
        }
    }

    public static void main(String[] args) { // 메인메소드
        new Paint(); // Paint클래스의 디폴트(=Default)생성자 실행
    
        
    }
}
