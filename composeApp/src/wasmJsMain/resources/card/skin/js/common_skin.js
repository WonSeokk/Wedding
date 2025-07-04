// $(function(){
//   $('.no_save').bind("contextmenu", function (e) {
//         return false;
//     });
//
// });
//
$(function() {
  $('.layer_pop').delay(60000).fadeOut('slow');
});

$(document).ready(function() {
  $('.card_bgm').click(function() {
    bgm_player();
  });
});


$(document).ready(function() {
    var text_length1 = $('.gr_mini_txt').text();
    var text_length2 = $('.br_mini_txt').text();
    if(text_length1.length >= 5){
      $('.gr_mini_txt').css('width', 'auto');
    }else if(text_length1.length < 5){
      $('.gr_mini_txt').css('width', '44px');
    }

    if(text_length2.length >= 5){
      $('.br_mini_txt').css('width', 'auto');
    }else if(text_length2.length < 5){
      $('.br_mini_txt').css('width', '44px');
    }
});

$(function() {
  // 혼주에게 연락하기
  $('.call_box_btn .parents').click(function() {
    $('.call_black').show();
    $('.call_pop_down').show();
  });
});

// $(document).ready(function() {
//   $('.call_black').on('click',function(){
//     $('.call_black').hide();
//     $('.call_pop_down').hide();
//   });
//
//   $('.call_close').on('click',function(){
//     $('.call_black').hide();
//     $('.call_pop_down').hide();
//   });
//
//
//   // $('.call_close').click(function() {
//   //   $('.call_black').hide();
//   //   $('.call_pop_down').hide();
//   // });
// });
document.addEventListener("DOMContentLoaded", function() {
  var callBlackElement = document.querySelector(".call_black");
  var callCloseElement = document.querySelector(".call_close");

  if (callBlackElement && callCloseElement) {
    callBlackElement.addEventListener("click", function() {
      hideElements(".call_black");
      hideElements(".call_pop_down");
    });

    callCloseElement.addEventListener("click", function() {
      hideElements(".call_black");
      hideElements(".call_pop_down");
    });
  }
});

function hideElements(selector) {
  var elements = document.querySelectorAll(selector);
  elements.forEach(function(element) {
    element.style.display = "none";
  });
}

$(function() {
  // 계좌 접었다 펴기
  $('.account_tit').click(function() {
    // $(this).toggleClass('on').next().toggleClass('on');
    $(this).siblings().slideToggle();
    $(this).toggleClass('on').next().toggleClass('on');
  });


  function copyToClipboard(val) {
    var t = document.createElement("textarea");
    document.body.appendChild(t);
    t.value = val;
    t.select();
    document.execCommand('copy');
    document.body.removeChild(t);
  }

  $('#copybtn1').click(function() {
    var accountType = $('#type').val();
    if (accountType == '1') {
      var account = $('#gr_account').val();
      if (!account) {
        alert('계좌번호를 이용하지 않습니다.');
      } else {
        copyToClipboard(account);
        alert('계좌번호를 복사하였습니다');
      }
    } else if (accountType == '0') {
      var account = $('#skin_gr_account').val();
      if (!account) {
        alert('계좌번호를 이용하지 않습니다.');
      } else {
        copyToClipboard(account);
        alert('계좌번호를 복사하였습니다');
      }
    }
  });

  $('#copybtn2').click(function() {
    var accountType2 = $('#type').val();
    if (accountType2 == '1') {
      var account2 = $('#gr_pa_account').val();
      if (!account2) {
        alert('계좌번호를 이용하지 않습니다.');
      } else {
        copyToClipboard(account2);
        alert('계좌번호를 복사하였습니다');
      }
    } else if (accountType2 == '0') {
      var account2 = $('#skin_gr_pa_account').val();
      if (!account2) {
        alert('계좌번호를 이용하지 않습니다.');
      } else {
        copyToClipboard(account2);
        alert('계좌번호를 복사하였습니다');
      }
    }
  });
  $('#copybtn3').click(function() {
    var accountType3 = $('#type').val();
    if (accountType3 == '1') {
      var account3 = $('#gr_pa2_account').val();
      if (!account3) {
        alert('계좌번호를 이용하지 않습니다.');
      } else {
        copyToClipboard(account3);
        alert('계좌번호를 복사하였습니다');
      }
    } else if (accountType3 == '0') {
      var account3 = $('#skin_gr_pa2_account').val();
      if (!account3) {
        alert('계좌번호를 이용하지 않습니다.');
      } else {
        copyToClipboard(account3);
        alert('계좌번호를 복사하였습니다');
      }
    }
  });

  $('#copybtn4').click(function() {
    var accountType4 = $('#type').val();
    if (accountType4 == '1') {
      var account4 = $('#br_account').val();
      if (!account4) {
        alert('계좌번호를 이용하지 않습니다.');
      } else {
        copyToClipboard(account4);
        alert('계좌번호를 복사하였습니다');
      }
    } else if (accountType4 == '0') {
      var account4 = $('#skin_br_account').val();
      if (!account4) {
        alert('계좌번호를 이용하지 않습니다.');
      } else {
        copyToClipboard(account4);
        alert('계좌번호를 복사하였습니다');
      }
    }
  });

  $('#copybtn5').click(function() {
    var accountType5 = $('#type').val();
    if (accountType5 == '1') {
      var account5 = $('#br_pa_account').val();
      if (!account5) {
        alert('계좌번호를 이용하지 않습니다.');
      } else {
        copyToClipboard(account5);
        alert('계좌번호를 복사하였습니다');
      }
    } else if (accountType5 == '0') {
      var account5 = $('#skin_br_pa_account').val();
      if (!account5) {
        alert('계좌번호를 이용하지 않습니다.');
      } else {
        copyToClipboard(account5);
        alert('계좌번호를 복사하였습니다');
      }
    }
  });
  $('#copybtn6').click(function() {
    var accountType6 = $('#type').val();
    if (accountType6 == '1') {
      var account6 = $('#br_pa2_account').val();
      if (!account6) {
        alert('계좌번호를 이용하지 않습니다.');
      } else {
        copyToClipboard(account6);
        alert('계좌번호를 복사하였습니다');
      }
    } else if (accountType6 == '0') {
      var account6 = $('#skin_br_pa2_account').val();
      if (!account6) {
        alert('계좌번호를 이용하지 않습니다.');
      } else {
        copyToClipboard(account6);
        alert('계좌번호를 복사하였습니다');
      }
    }
  });


  //url 복사
  $('.url_btn').click(function() {
    var urlType = $('#type').val();
    if (urlType == '1') {
      var editor_url = $('#card_id').val();
      if (!editor_url) {
        alert('주소를 입력하지 않았습니다.');
      } else {
        copyToClipboard("https://directwedcard.com/" + editor_url);
        alert('주소를 복사하였습니다');
      }
    } else if (urlType == '0') {
      var skin_url = $('#skin_id').val();
      if (!skin_url) {
        alert('주소를 입력하지 않았습니다.');
      } else {
        copyToClipboard("https://directwedcard.com/" + skin_url);
        alert('주소를 복사하였습니다');
      }
    }
  })
});


$(function() {
  // 참석여부 끄기
  $('.check_attend').click(function() {
    $('.attend_black').show();
    $('.attend_pop_wrap').show();
  });

  $('.attend_black').click(function() {
    $('.attend_black').hide();
    $('.attend_pop_wrap').hide();
  });

  $('.attend_close').click(function() {
    $('.attend_black').hide();
    $('.attend_pop_wrap').hide();
  });

  //체크 박스 선택시 숨김
  $("input:radio[name=invite_check]").change(function() {
    var card_check = $(this).val();
    if (card_check == 'T') {
      $('.delivery_box').show();
    } else {
      $('.delivery_box').hide();
    }
  });

  $("#btn_address").click(function() {
    new daum.Postcode({
      oncomplete: function(data) {
        // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

        // 각 주소의 노출 규칙에 따라 주소를 조합한다.
        // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
        var fullAddr = ''; // 최종 주소 변수
        var extraAddr = ''; // 조합형 주소 변수

        // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
        if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
          fullAddr = data.roadAddress;

        } else { // 사용자가 지번 주소를 선택했을 경우(J)
          fullAddr = data.jibunAddress;
        }

        // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
        if (data.userSelectedType === 'R') {
          //법정동명이 있을 경우 추가한다.
          if (data.bname !== '') {
            extraAddr += data.bname;
          }
          // 건물명이 있을 경우 추가한다.
          if (data.buildingName !== '') {
            extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
          }
          // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
          fullAddr += (extraAddr !== '' ? ' (' + extraAddr + ')' : '');
        }

        // 우편번호와 주소 정보를 해당 필드에 넣는다.
        document.getElementById('delivery_addr1').value = data.zonecode; //5자리 새우편번호 사용
        document.getElementById('delivery_addr2').value = fullAddr;

        // 커서를 상세주소 필드로 이동한다.
        document.getElementById('delivery_addr3').focus();
      }
    }).open();
  });
});

function guest_book_del(ci) {
  if ($('#del_password').val() == '') {
    alert('방명록 삭제 비밀번호를 입력해주세요.');
    return;
  }

  var g_idx = $('#del_no').val();

  var form_data = new FormData();
  form_data.append('mode', 'del');
  form_data.append('card_id', ci);
  form_data.append('g_pw', $('#del_password').val());
  form_data.append('g_idx', g_idx);

  $.ajax({
    url: "/card/skin/guest_book_proc.php",
    type: "post",
    processData: false,
    contentType: false,
    data: form_data,
    success: function(data) {
      if (data == '-1') {
        alert('방명록 비밀번호를 설정해주세요.');
        return;
      } else if (data == '-2') {
        alert('방명록 비밀번호를 확인해주세요.');
        return;
      } else if(data == '-3'){
        alert('초대장 정보가 확인 되지 않습니다.');
      } else if (data == '1') {
        $('.book_inner' + g_idx).remove();
        alert('삭제가 완료되었습니다.');

        var b_l = $('.book_inner').length;
        if (b_l == 0) {
          var html = '<p class="nothing_book"><i class="xi-paper-o"></i>아직 작성된 방명록이 없습니다.</p>';
          $('.book_box').prepend(html);
        }

        $('.del_wrap').hide();
        $('.black_back').hide();
        $('.guest_book_pop').hide();
      }
    }
  });
}

function guest_book_write(ci) {
  if ($('#write_name').val() == '') {
    alert('성함을 입력해주세요.');
    return;
  }
  if ($('#write_txt').val() == '') {
    alert('내용을 입력해주세요.');
    return;
  }

  $.ajax({
    type: "post",
    url: "/card/skin/guest_book_proc.php",
    data: {
      mode: 'add',
      card_id: ci,
      g_name: $('#write_name').val(),
      g_content: $('#write_txt').val()
    },
    dataType: "json",
    success: function(data) {
      if (data == '-1') {

      }else if(data == '-3'){
        alert('초대장 정보가 확인 되지 않습니다.');
      }else if(data == '-99'){
        alert('금지어작성');
      } else {
        var nothing = $('.nothing_book').length;
        if (nothing != 0) {
          $('.nothing_book').remove();
        }
        var guestType = $('.guest_book_inner').attr('data-type');
        if (guestType == 'guest_book1') {
          data.forEach(function(element) {
            var html = '<div class="book_inner book_inner' + element.idx + '">';
            html += '<div class="book_txt">';
            html += '<p><span class="from_em theme_1_color">FROM.</span> ' + element.g_name + '</p>';
            html += '<span>' + element.g_content + '</span>';
            html += '</div>';
            html += '<div class="book_mark">';
            html += '<span class="book_date">' + element.reg_date + '</span>';
            html += '<span class="book_del" onclick="book_del_ac(' + element.idx + ');"><i class="xi-close-thin"></i></span>';
            html += '</div>';
            html += '</div>';
            // console.log($('.book_box .book_inner').length);
            if ($('.book_box .book_inner').length >= '3') {
              $('.book_box .book_inner').last().remove();
              $('.book_box').prepend(html);
            } else {
              $('.book_box').prepend(html);
            }

            var html2 = '<div class="book_inner book_inner' + element.idx + '">';
            html2 += '<div class="book_tit">';
            html2 += '<p><span class="from_em theme_1_color">FROM.</span> ' + element.g_name + '</p>';
            html2 += '<p><span class="book_date">' + element.reg_date + '</span>';
            html2 += '<span class="book_del" onclick="book_del_ac(' + element.idx + ');"><i class="xi-close-thin"></i></span></p>';
            html2 += '</div>';
            html2 += '<p class="pop_txt">' + element.g_content + '</p>';
            html2 += '</div>';
            $('.book_title').after(html2);
          })
        } else {
          $('.guest_swiper').show();
          data.forEach(function(element) {
            var html = '<div class="swiper-slide book_slide_box book_inner book_inner' + element.idx + '">';
            html += '<div class="book_article">';
            html += '<p class="book_who"><span class="from_em theme_1_color">FROM.</span> ' + element.g_name + '<span class="book_del book_del2" onclick="book_del_ac(' + element.idx + ');"><i class="xi-close-thin"></i></span></p>';
            html += '<p class="book_slide_txt">' + element.g_content + '</p>';
            html += '<p class="book2_date"><span>' + element.reg_date + '</span></p>'
            html += '</div>';
            html += '</div>';
            if ($('.book_box .book_inner').length >= '10') {
              $('.book_box .book_inner').last().remove();
              $('.guest_swiper .swiper-wrapper').prepend(html);
            } else {
              $('.guest_swiper .swiper-wrapper').prepend(html);
            }

            var html2 = '<div class="book_inner book_inner' + element.idx + '">';
            html2 += '<div class="book_tit">';
            html2 += '<p><span class="from_em theme_1_color">FROM.</span> ' + element.g_name + '</p>';
            html2 += '<p><span class="book_date">' + element.reg_date + '</span>';
            html2 += '<span class="book_del" onclick="book_del_ac(' + element.idx + ');"><i class="xi-close-thin"></i></span></p>';
            html2 += '</div>';
            html2 += '<p class="pop_txt">' + element.g_content + '</p>';
            html2 += '</div>';
            $('.book_title').after(html2);
          })
          guestbookLoad();
        }


        $('.write_wrap').hide();
        $('.black_back').hide();
      }
    }
  });
}

function sign_check(ci) {
  if ($('#attend_name').val() == '') {
    alert('성함을 입력해주세요.');
    return;
  }

  if ($('#sign-mobile1').val() == '') {
    alert('연락처를 입력해주세요.');
    return;
  }

  if ($('#sign-mobile2').val() == '') {
    alert('연락처를 입력해주세요.');
    return;
  }

  if ($('#sign-mobile3').val() == '') {
    alert('연락처를 입력해주세요.');
    return;
  }

  // if ($("input[name='invite_check']:checked").val() == 'T') {
  //   if ($('#delivery_addr1').val() == '') {
  //     alert('우편번호를 입력해주세요.');
  //     return;
  //   }
  //
  //   if ($('#delivery_addr2').val() == '') {
  //     alert('주소를 입력해주세요.');
  //     return;
  //   }
  //
  //   if ($('#delivery_addr3').val() == '') {
  //     alert('주소를 입력해주세요.');
  //     return;
  //   }
  //
  //   var sign_zip = $('#delivery_addr1').val();
  //   var sign_addr = $('#delivery_addr2').val();
  //   var sign_addr2 = $('#delivery_addr3').val();
  // } else {
    var sign_zip = '';
    var sign_addr = '';
    var sign_addr2 = '';
  // }


  // if (!$('#own_agree').is(':checked')) {
  //   alert('개인정보 수집 및 이용동의 해주시기 바랍니다.');
  //   return;
  // }

  var card_id = ci;
  var sign_g_b = $("input[name='check_groom_bride']:checked").val();
  var sign_check = $("input[name='attend_check']:checked").val();
  var name = $('#attend_name').val();
  var mobile = $('#sign-mobile1').val() + '-' + $('#sign-mobile2').val() + '-' + $('#sign-mobile3').val();
  var sign_food = $("input[name='attend_food']:checked").val();
  // var sign_card = $("input[name='invite_check']:checked").val();
  var sign_card = 'F';
  var sign_group = $('#attend_group').val();

  var form_data = new FormData();
  form_data.append('card_id', card_id);
  form_data.append('sign_g_b', sign_g_b);
  form_data.append('sign_check', sign_check);
  form_data.append('name', name);
  form_data.append('mobile', mobile);
  form_data.append('sign_food', sign_food);
  form_data.append('sign_card', sign_card);
  form_data.append('sign_zip', sign_zip);
  form_data.append('sign_addr', sign_addr);
  form_data.append('sign_addr2', sign_addr2);
  form_data.append('sign_group', sign_group);

  $.ajax({
    url: "/card/skin/sign_check_proc.php",
    type: "post",
    processData: false,
    contentType: false,
    data: form_data,
    success: function(data) {
      if (data == '1') {
        alert('입력 완료 되었습니다.');
        $('.attend_black').hide();
        $('.attend_pop_wrap').hide();
      }
    }
  });
}

// 스크롤시 애니메이션효과
$(function() {
  var scrollType = $('#type').val();
  if (scrollType == '1') {
    $('#phone').scroll(function() {
      $('.inner_padding').each(function(i) {

        var bottom_of_element = $(this).offset().top + 150;
        var bottom_of_window = $(window).scrollTop() + $(window).height();

        if (bottom_of_window > bottom_of_element) {
          $(this).animate({
            'opacity': '1',
            'margin-top': '0'
          }, 1100);
        }

      });
    });
  } else if (scrollType == '0') {
    $(window).scroll(function() {
      $('.inner_padding').each(function(i) {

        var bottom_of_element = $(this).offset().top + 80;
        var bottom_of_window = $(window).scrollTop() + $(window).height();

        if (bottom_of_window > bottom_of_element) {
          $(this).animate({
            'opacity': '1',
            'margin-top': '0'
          }, 1100);
        }

      });
    });
  }
});


function bgm_player() {
  var bgm = document.getElementById("skin_bgm");
    bgm.muted = false;
  if (bgm.paused) {
    $(".card_bgm").find('img').attr("src", "/card/skin/img/pause.svg");
    bgm.play();
  } else {
    $(".card_bgm").find('img').attr("src", "/card/skin/img/play.svg");
    bgm.pause();
  }
}

function kCalendar(id, date) {
  var kCalendar = document.getElementById(id);


  if (typeof(date) !== 'undefined') {
    date = date.split('-');
    date[1] = date[1] - 1;
    date = new Date(date[0], date[1], date[2]);
  } else {
    var date = new Date();
  }

  var currentYear = date.getFullYear();
  //년도를 구함

  var currentMonth = date.getMonth() + 1;
  //연을 구함. 월은 0부터 시작하므로 +1, 12월은 11을 출력

  var currentDate = date.getDate();
  //오늘 일자.

  date.setDate(1);
  var currentDay = date.getDay();
  //이번달 1일의 요일은 출력. 0은 일요일 6은 토요일

  var dateString = new Array('sun', 'mon', 'tue', 'wed', 'thu', 'fri', 'sat');
  var lastDate = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
  if ((currentYear % 4 === 0 && currentYear % 100 !== 0) || currentYear % 400 === 0)
    lastDate[1] = 29;
  //각 달의 마지막 일을 계산, 윤년의 경우 년도가 4의 배수이고 100의 배수가 아닐 때 혹은 400의 배수일 때 2월달이 29일 임.

  var currentLastDate = lastDate[currentMonth - 1];
  var week = Math.ceil((currentDay + currentLastDate) / 7);
  //총 몇 주인지 구함.

  if (currentMonth != 1)
    var prevDate = currentYear + '-' + (currentMonth - 1) + '-' + currentDate;
  else
    var prevDate = (currentYear - 1) + '-' + 12 + '-' + currentDate;
  //만약 이번달이 1월이라면 1년 전 12월로 출력.

  if (currentMonth != 12)
    var nextDate = currentYear + '-' + (currentMonth + 1) + '-' + currentDate;
  else
    var nextDate = (currentYear + 1) + '-' + 1 + '-' + currentDate;
  //만약 이번달이 12월이라면 1년 후 1월로 출력.


  if (currentMonth < 10)
    var currentMonth = '0' + currentMonth;
  //10월 이하라면 앞에 0을 붙여준다.

  var calendar = '';

  calendar += '<div id="header">';
  calendar += '<span id="date">' + currentMonth + '<font>월</font></span>';
  calendar += '</div>';
  calendar += '<table border="0" cellspacing="0" cellpadding="0">';
  calendar += '<caption>' + currentYear + '년 ' + currentMonth + '월 달력</caption>';
  calendar += '<tbody>';

  var dateNum = 1 - currentDay;

  for (var i = 0; i < week; i++) {
    calendar += '			<tr>';
    for (var j = 0; j < 7; j++, dateNum++) {
      if (dateNum < 1 || dateNum > currentLastDate) {
        calendar += '				<td class="' + dateString[j] + '"> </td>';
        continue;
      }
      if (dateNum == currentDate) {
        calendar += '				<td class="' + dateString[j] + '"><div class="weddingday theme_1_back">' + dateNum + '</div></td>';
      } else {
        calendar += '				<td class="' + dateString[j] + '">' + dateNum + '</td>';
      }
    }
    calendar += '			</tr>';
  }
  calendar += '			</tbody>';
  calendar += '		</table>';

  kCalendar.innerHTML = calendar;
}



function wdDday(t, w) {
  var dat1 = new Date(t);
  var dat2 = new Date(w);
  var diff = dat2 - dat1;
  var currDay = 24 * 60 * 60 * 1000; // 시 * 분 * 초 * 밀리세컨

  if (dat2 < dat1) {
    $('#wd_d-day').text("이 지났습니다");
  } else if (dat2 > dat1) {
    $('#wd_d-day').text(" " + parseInt(diff / currDay) + "일 전");
  } else if (dat2 = dat1) {
    $('#wd_d-day').text("이 오늘입니다");
  }
}

// function wdDday(t, w) {
//   var strDate1 = t;
//   var strDate2 = w;
//   var arr1 = strDate1.split('-');
//   var arr2 = strDate2.split('-');
//   var dat1 = new Date(arr1[0], arr1[1], arr1[2]);
//   var dat2 = new Date(arr2[0], arr2[1], arr2[2]);
//
//   var diff = dat2 - dat1;
//   var currDay = 24 * 60 * 60 * 1000; // 시 * 분 * 초 * 밀리세컨
//
//   // $('#wd_d-day').text(parseInt(diff/currDay) + " 일");
//
//   // if (dat2 < dat1) {
//   //   $('#wd_d-day').text("이 지났습니다");
//   // } else {
//   //   $('#wd_d-day').text(" " + parseInt(diff / currDay-1) + "일 전");
//   // }
//
//   if (dat2 < dat1) {
//     $('#wd_d-day').text("이 지났습니다");
//   } else if (dat2 > dat1) {
//     $('#wd_d-day').text(" " + parseInt(diff / currDay) + "일 전");
//   } else if (dat2 = dat1) {
//     $('#wd_d-day').text("이 오늘입니다");
//   }
// }


function loadColorChange(){
  var loadColor_1 = $('#theme_main').val();
  var loadColor_2 = $('#theme_sub').val();
  var loadFont = $('#font_type').val();

  $('.theme_1_back').attr('style', function(i,s) { return (s || '') + 'background: '+loadColor_1+' !important;' });
  $('.theme_1_color').attr('style', function(i,s) { return (s || '') + 'color: '+loadColor_1+' !important;' });
  $('.theme_1_border').attr('style', function(i,s) { return (s || '') + 'border: 1px solid'+loadColor_1+' !important;' });

  $('.theme_2_back').attr('style', function(i,s) { return (s || '') + 'background: '+loadColor_2+' !important;' });
  $('.theme_2_color').attr('style', function(i,s) { return (s || '') + 'color: '+loadColor_2+' !important;' });
  $('.theme_2_border').attr('style', function(i,s) { return (s || '') + 'border: 1px solid'+loadColor_2+' !important;' });

  $('#invite_wrap *').css('font-family', loadFont);
}

function flower_c(uid,idx){
  var form_data = new FormData();
  form_data.append('uid', uid);

  $.ajax({
    url: "/card/skin/flower_proc.php",
    type: "post",
    processData: false,
    contentType: false,
    data: form_data,
    success: function(data) {
      location.href="https://partner.flabiz.kr/bms/event/oi?cfcard="+idx;
        // location.href="http://agent.maruw.co.kr/mobile/product?shop=feel_direct&idx="+uid+"&goods_div_idx=1&callback_url=https://directwedcard.com/class.cardApi.php?idx="+uid;
    }
  });
}
