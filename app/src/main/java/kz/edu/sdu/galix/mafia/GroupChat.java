package kz.edu.sdu.galix.mafia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GroupChat extends AppCompatActivity {
    LinearLayout groupChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        groupChat = (LinearLayout) findViewById(R.id.groupChat);
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
        String m[] = {"можность выбрать стиль:",
                "нести совсем необязательно. Более того",
            "Самым известным «рыбным» текстом является", "Hello", "Считается, что впервые его применили в книгопечатании еще в XVI веке. Своим появлением Lorem ipsum обязан древнеримскому философу Цицерону, ведь именно из его трактата «О пределах добра и зла» средневековый книгопечатник вырвал отдельные фразы и слова, получив текст-«рыбу», широко используемый и по сей день",
            "И даже с языками, в различных языках те или иные буквы встречаются с разной частотой, имеется разница в длине наиболее распространенных слов. Отсюда напрашивается вывод, что все же лучше использова есарианты текста на основе оригинального трактата,"};
        for(int i = 0; i < m.length; i++) {
            TextView textView = new TextView(this);
            LayoutInflater inflater = getLayoutInflater();
            int layout = R.layout.chat_item_left;
            if(i % 2 == 1) {
                layout = R.layout.chat_item_right;
            }
            View view = inflater.inflate(layout, groupChat, false);
            TextView tv = ((TextView)view.findViewById(R.id.chat_message));
            tv.setText(m[i]);
            LinearLayout ll = (LinearLayout)view.findViewById(R.id.chat_item_layout);

            ll.setPadding(15,15,15,15);
            groupChat.addView(view);
            textView.setLayoutParams(rlp);
            groupChat.addView(textView);
        }
    }

}
