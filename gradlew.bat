����   2 i bcom/ponko/cn/ui/account/exchange_product/ExchangeProductsAdapter$ExchangeProductsHolder$bindView$1  java/lang/Object  !android/view/View$OnClickListener  onClick (Landroid/view/View;)V this$0 YLcom/ponko/cn/ui/account/exchange_product/ExchangeProductsAdapter$ExchangeProductsHolder; 	 
	   Wcom/ponko/cn/ui/account/exchange_product/ExchangeProductsAdapter$ExchangeProductsHolder  getView ()Landroid/view/View;  
   android/view/View  
getContext ()Landroid/content/Context;  
   	clipboard  android/content/Context  getSystemService &(Ljava/lang/String;)Ljava/lang/Object;  
   kotlin/TypeCastException ! Bnull cannot be cast to non-null type android.text.ClipboardManager # <init> (Ljava/lang/String;)V % &
 " ' android/text/ClipboardManager ) $bean 'Lcom/ponko/cn/bean/ExchangeProductBean; + ,	  - %com/ponko/cn/bean/ExchangeProductBean / 	getPostid ()Ljava/lang/String; 1 2
 0 3 java/lang/CharSequence 5 setText (Ljava/lang/CharSequence;)V 7 8
 * 9 已复制到剪贴板 ; com/ponko/cn/module/ToastUtil = 	showToast ? &
 > @ Landroid/text/ClipboardManager; this dLcom/ponko/cn/ui/account/exchange_product/ExchangeProductsAdapter$ExchangeProductsHolder$bindView$1; it Landroid/view/View; �(Lcom/ponko/cn/ui/account/exchange_product/ExchangeProductsAdapter$ExchangeProductsHolder;Lcom/ponko/cn/bean/ExchangeProductBean;)V ()V % H
  I Lkotlin/Metadata; mv       bv        k    d1 4��
��

��

��02
 *00H
¢ d2 <anonymous>   kotlin.jvm.PlatformType bindView *(Lcom/ponko/cn/bean/ExchangeProductBean;)V Z [ @com/ponko/cn/ui/account/exchange_product/ExchangeProductsAdapter ] ExchangeProductsHolder ExchangeProductsAdapter.kt Code LocalVariableTable LineNumberTable StackMapTable 
SourceFile EnclosingMethod InnerClasses RuntimeVisibleAnnotations 0      	 
   + ,        a   �     5*� � � �  Y� � "Y$� (�� *M,*� .� 4� 6� :<� A�    b      !   B    5 C D     5 E F  c       9 ! : / ; 4 < d    ]    % G  a        *+� *,� .*� J�      e    ` f     \ g            ^ _  h   F  K  L[ I MI MI N O[ I MI PI Q RI S