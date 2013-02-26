/**
 * 
 */
package com.onyx.android.sample.data;

/**
 * @author joy
 *
 */
public interface IFileOperationHandler
{
    void onNewFile();
    void onNewFolder();
    void onRename();
    void onCopy();
    void onCut();
    void onRemove();
    void onProperty();
    void onGotoFolder();
    void onFavorite();
    void onShowOpenWith();
}
